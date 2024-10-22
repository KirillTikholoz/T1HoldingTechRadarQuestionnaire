package org.example.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.ErrorResponseDto;
import org.example.utils.JwtTokenUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                Jwt jwt = jwtDecoder.decode(jwtToken);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<SimpleGrantedAuthority> roles = new LinkedList<>();
                    roles.add(new SimpleGrantedAuthority(jwtTokenUtils.getRole(jwtToken)));

                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, roles);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (BadJwtException e) {
                createUnauthorizedResponse(response);
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void createUnauthorizedResponse(@NonNull HttpServletResponse response) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDto errorResponse =
                new ErrorResponseDto("Unauthorized", "Invalid or missing JWT token");

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
