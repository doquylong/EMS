package com.bart.test.service;

import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dto.request.CreateUserRequest;
import com.bart.test.dto.request.LoginRequest;
import com.bart.test.dto.response.AppResponse;

public interface UserService {
    AppResponse createUser(CreateUserRequest request);
    AppResponse getAll();
    AppResponse getDetail(Long id);
    AppResponse getProfile(UserPrincipal me);
}
