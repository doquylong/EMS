package com.bart.test.service;

import com.bart.test.dto.request.LoginRequest;
import com.bart.test.dto.response.AppResponse;
import org.springframework.stereotype.Service;

public interface AuthService {
    AppResponse login(LoginRequest loginRequest);
}
