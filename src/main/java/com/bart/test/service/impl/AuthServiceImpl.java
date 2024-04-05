package com.bart.test.service.impl;

import com.bart.test.config.security.JwtUtils;
import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dto.request.LoginRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.dto.response.LoginResponse;
import com.bart.test.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AppResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        log.info("Login success -user's email: {}",userDetails.getEmail());
        LoginResponse data = LoginResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .token(token).build();
        return AppResponse.ok(data);
    }
}
