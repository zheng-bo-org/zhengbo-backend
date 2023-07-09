package org.zhengbo.backend.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.cache.Cache;
import org.zhengbo.backend.cache.prefixs.UserWebAccessToken;
import org.zhengbo.backend.global_exceptions.UserAuthException;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.service.user.TokenService;
import org.zhengbo.backend.utils.JSON;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenImpl implements TokenService {
    private final Cache cache;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(
            Map<String, String> extraClaims,
            CustomUserDetails customUserDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(combineUsername(new CombinedUsername(customUserDetails.userId(), customUserDetails.username(), customUserDetails.userType())))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    record ExtraJsonClaims(Long userId, String username, String pwd, TypeOfUser type) {

    }

    public String generateToken(
            CustomUserDetails customUserDetails
    ) {
        var json = new ExtraJsonClaims(customUserDetails.userId(), customUserDetails.username(), customUserDetails.pwd(), customUserDetails.userType());
        HashMap<String, String> extraClaims = new HashMap<>(1) {{
            put("json", JSON.toJson(json));
        }};
        return buildToken(extraClaims, customUserDetails, jwtExpiration);
    }

    record Token(Long userId, List<String> tokens) {

    }

    @Override
    public boolean isAbleToCreateNewTokenForTheUser(Long userId) {
        var token = cache.getJson(UserWebAccessToken.class, userId.toString(), Token.class);
        return token.map(value -> value.tokens.size() == 0).orElse(true);
    }

    @Override
    public String signToken(CustomUserDetails customUserDetails) throws RuntimeException {
        var token = generateToken(customUserDetails);
        var tokenInTheCache = cache.getJson(UserWebAccessToken.class, customUserDetails.userId().toString(), Token.class).orElse(new Token(customUserDetails.userId(), new ArrayList<>(2)));
        tokenInTheCache.tokens.add(token);
        cache.setJson(UserWebAccessToken.class, customUserDetails.userId().toString(), tokenInTheCache, refreshExpiration);
        return token;
    }

    @Override
    public boolean isTokenValid(String token, CustomUserDetails customUserDetails) {
        var tokenInCache = cache.getJson(UserWebAccessToken.class, customUserDetails.userId().toString(), Token.class);
        if (tokenInCache.isEmpty() || !tokenInCache.get().tokens.contains(token)) {
            return false;
        }

        final String username = extractUsername(token);
        CombinedUsername combinedUsername = deCombineUsername(username);
        return (
                combinedUsername.type().equals(customUserDetails.userType()) &&
                        combinedUsername.username().equals(customUserDetails.username()) &&
                        !isTokenExpired(token)
        );
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public void makeTheTokenInvalid(String token) {
        var user = tokenToUserDetails(token);
        var tokenInCache = cache.getJson(UserWebAccessToken.class, user.userId().toString(), Token.class);
        if (tokenInCache.isEmpty()) {
            return;
        }
        tokenInCache.get().tokens.removeIf(t -> t.equals(token));
        cache.setJson(UserWebAccessToken.class, user.userId().toString(), new Token(tokenInCache.get().userId, tokenInCache.get().tokens()), jwtExpiration);
    }

    @Override
    public void makeCurrentTokenInvalid() {
        String currentToken = getCurrentToken();
        makeTheTokenInvalid(currentToken);
    }

    @Override
    public CustomUserDetails tokenToUserDetails(String token) {
        var json =  (String) extractAllClaims(token).get("json");
        var extraClaims = JSON.fromJson(json, ExtraJsonClaims.class);
        return new CustomUserDetails(extraClaims.userId, extraClaims.username, extraClaims.pwd, extraClaims.type, null);
    }

    @Override
    public CombinedUsername deCombineUsername(String strOfUsernameAndUserTypeCombined) {
        var usernameAndUserType = strOfUsernameAndUserTypeCombined.split(",");
        var userId = usernameAndUserType[0];
        var username = usernameAndUserType[1];
        var userType = usernameAndUserType[2];
        TypeOfUser type = TypeOfUser.valueOf(userType);

        return new CombinedUsername(Long.parseLong(userId), username, type);
    }

    @Override
    public String combineUsername(CombinedUsername username) {
        return username.userId() + "," + username.username() + "," + username.type().name();
    }

    private void avoidAnonymousUser(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserAuthException(UserAuthException.UserAuthExceptionCode.UN_AUTHENTICATED, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Long getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        avoidAnonymousUser(authentication);
        CustomUserDetails userDetails = (TokenService.CustomUserDetails)authentication.getPrincipal();
        return userDetails.userId();
    }

    private Authentication getNonAnonymousUserAuthInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        avoidAnonymousUser(authentication);
        return authentication;
    }

    private String getCurrentToken() {
        var auth = getNonAnonymousUserAuthInfo();
        var credentials = (HashMap<String, String>) auth.getCredentials();
        return credentials.get("jwt");
    }

    @Override
    public String refreshTokenForCurrentUser() {
        var currentToken = getCurrentToken();
        var userDetails = tokenToUserDetails(currentToken);
        String signedToken = signToken(userDetails);
        makeTheTokenInvalid(currentToken);
        return signedToken;
    }
}
