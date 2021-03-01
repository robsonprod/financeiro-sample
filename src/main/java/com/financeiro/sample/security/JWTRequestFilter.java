package com.financeiro.sample.security;

import com.financeiro.sample.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final AuthService authService;

    JWTRequestFilter(AuthService authService) {
        this.authService = authService;
    }

    private void authenticationFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        log.info("FilterChain {} - {}", response.getLocale(), chain.getClass());
        Optional<Authentication> authentication = this.authService.authenticate((HttpServletRequest) request);
        authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
            authenticationFilter(request, response, chain);
        }

        chain.doFilter(request, response);
    }
}
