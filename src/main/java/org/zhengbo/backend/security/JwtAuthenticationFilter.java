package org.zhengbo.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zhengbo.backend.service.user.TokenService;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var path = request.getServletPath();
        var allowAccess = TokenNotRequiredApiScanner.getWhiteList().stream().anyMatch(allowedPath -> path.matches(allowedPath.replace("**", ".*").replace("*", "[^/]*")));
        if (allowAccess) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        try {
            final String jwt;
            jwt = authHeader.substring(7);
            var userDetailsFromJwt = tokenService.tokenToUserDetails(jwt);
            if (userDetailsFromJwt.username() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(tokenService.combineUsername(new TokenService.CombinedUsername(userDetailsFromJwt.userId(), userDetailsFromJwt.username(), userDetailsFromJwt.userType())));
                if (tokenService.isTokenValid(jwt, (TokenService.CustomUserDetails) userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            new HashMap<String, String>() {{
                                put("jwt", jwt);
                            }},
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else {
                    throw new RuntimeException("Invalid jwt");
                }
            }
        }catch (Exception ex) {
            log.error("Jwt validation failed. The error is: {}", ex.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String timestamp = Instant.ofEpochMilli(System.currentTimeMillis()).toString();
            String json = String.format("{\"timestamp\": \"%s\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Access Denied\", \"path\": \"%s\"}", timestamp, path);
            response.getWriter().write(json);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
