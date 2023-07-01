package org.zhengbo.backend.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.cache.Cache;
import org.zhengbo.backend.cache.prefixs.UserWebAccessToken;
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
                .setSubject(combineUsername(new CombinedUsername(customUserDetails.username(), customUserDetails.userType())))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(
            CustomUserDetails customUserDetails
    ) {
        var json = JSON.toJson(customUserDetails);
        HashMap<String, String> extraClaims = new HashMap<>(1) {{
            put("json", json);
        }};
        return buildToken(extraClaims, customUserDetails, jwtExpiration);
    }

    record Token(Long userId, List<String> tokens) {

    }

    @Override
    public boolean isAbleToCreateNewTokenForTheUser(Long userId) {
        var token = cache.getJson(UserWebAccessToken.class, userId.toString(), Token.class);
        return token == null || token.tokens.size() == 0;
    }

    @Override
    public String signToken(CustomUserDetails customUserDetails) throws RuntimeException {
        var token = generateToken(customUserDetails);
        List<String> tokens = new ArrayList<>(1);
        tokens.add(token);

        cache.setJson(UserWebAccessToken.class, customUserDetails.userId().toString(), new Token(customUserDetails.userId(), tokens), refreshExpiration);
        return token;
    }

    @Override
    public boolean isTokenValid(String token, CustomUserDetails customUserDetails) {
        var tokenInCache = cache.getJson(UserWebAccessToken.class, customUserDetails.userId().toString(), Token.class);
        if (tokenInCache == null) {
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
        cache.removeKey(UserWebAccessToken.class, user.userId().toString());
    }

    @Override
    public CustomUserDetails tokenToUserDetails(String token) {
        var json = (String) extractAllClaims(token).get("json");
        return JSON.fromJson(json, CustomUserDetails.class);
    }

    @Override
    public CombinedUsername deCombineUsername(String strOfUsernameAndUserTypeCombined) {
        var usernameAndUserType = strOfUsernameAndUserTypeCombined.split(",");
        var username = usernameAndUserType[0];
        var userType = usernameAndUserType[1];
        TypeOfUser type = TypeOfUser.valueOf(userType);

        return new CombinedUsername(username, type);
    }

    @Override
    public String combineUsername(CombinedUsername username) {
        return username.username() + "," + username.type().name();
    }
}
