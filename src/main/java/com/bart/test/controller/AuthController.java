package com.bart.test.controller;

import com.bart.test.dto.request.LoginRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // login api for every one
    @PostMapping("/v1/auth/login")
    public AppResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }
}
