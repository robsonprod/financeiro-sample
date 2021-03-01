package com.financeiro.sample.service;

import com.financeiro.sample.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class AuthService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtToken;

    public AuthService(CustomUserDetailsService userDetailsService, JwtTokenUtil jwtToken) {
        this.userDetailsService = userDetailsService;
        this.jwtToken = jwtToken;
    }

    public Optional<Authentication> authenticate(HttpServletRequest request) {
        return extractBearerTokenHeader(request).flatMap(this::lookup).map(authentication -> {
            // usado para obter o usuario nos endpoints @AuthenticatePricipal
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            return authentication;
        });
    }

    private Optional<UsernamePasswordAuthenticationToken> lookup(String token) {
        try {
            val user = jwtToken.getUsernameFromToken(token)
                    .map(userDetailsService::loadUserByUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Not found user"));

            if (nonNull(user) && jwtToken.validateToken(token, user)) {
                val authentication = createAuthentication(user);
                return Optional.of(authentication);
            }

            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.warn("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token has expired");
        }

        return Optional.empty();
    }

    private static Optional<String> extractBearerTokenHeader(@NonNull HttpServletRequest request) {
        try {
            val authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (nonNull(authorization)) {
                if (authorization.startsWith(BEARER_PREFIX)) {
                    val token = authorization.substring(BEARER_PREFIX.length()).trim();

                    if (!token.isEmpty()) {
                        return Optional.of(token);
                    }
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            log.error("An unknown error occurred while trying to extract bearer token", e);
            return Optional.empty();
        }
    }

    private static UsernamePasswordAuthenticationToken createAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails,"N/A", userDetails.getAuthorities());
    }
}
