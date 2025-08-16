package com.gestion.exams.config;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
                         jakarta.servlet.http.HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException)
            throws java.io.IOException {
        response.sendError(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
