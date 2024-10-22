package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtTokenUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;
    private Claims getAllClaimsFromAccessToken(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Long getId(String token){
        return Long.parseLong(getAllClaimsFromAccessToken(token).getSubject());
    }
    public String getRole(String token){
        return getAllClaimsFromAccessToken(token).get("role", String.class);
    }
    public String extractTokenFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
        log.debug("The token is extracted from the context");

        return tokenValue;
    }
}
