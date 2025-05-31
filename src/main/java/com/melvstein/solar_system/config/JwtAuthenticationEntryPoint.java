package com.melvstein.solar_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.util.ApiResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<?> data = ApiResponseUtils.error(
                ApiConstants.RESPONSE_ERROR.get("code"),
                authException.getMessage(),
                null
        );

        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, data);
    }
}
