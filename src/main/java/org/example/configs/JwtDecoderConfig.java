package org.example.configs;

import lombok.RequiredArgsConstructor;
import org.example.utils.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfig {
    private final JwtTokenUtils jwtTokenUtils;
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtTokenUtils.getSignKey()).build();
    }
}
