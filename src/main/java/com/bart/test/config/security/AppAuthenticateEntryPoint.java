package com.bart.test.config.security;

import com.bart.test.dto.response.AppResponse;
import com.bart.test.exception.ErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppAuthenticateEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        boolean isJwtValid = request.getAttribute("isJwtValid") == null ? true : false;
        AppResponse appResponse = null;
        if (!isJwtValid){
            appResponse = new AppResponse(ErrorInfo.JWT_TOKEN_INVALID);
        }else {
            appResponse = new AppResponse(ErrorInfo.ACCESS_UNAUTHORIZED);
        }
        response(response,appResponse);
    }

    private void response (HttpServletResponse httpServletResponse, AppResponse appResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(200);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(appResponse));
    }
}
