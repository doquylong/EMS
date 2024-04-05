package com.bart.test.controller;

import com.bart.test.config.security.LoggedInUser;
import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dto.request.CreateUserRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // create user : teacher
    @PostMapping("/v1/user")
    public AppResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/v1/user")
    public AppResponse getListUser(){
        return userService.getAll();
    }

    @GetMapping("/v1/user/{id}")
    public AppResponse getUserDetail(@PathVariable Long id){
        return userService.getDetail(id);
    }

    @GetMapping("/v1/me")
    public AppResponse getProfile(@LoggedInUser UserPrincipal me){
        return userService.getProfile(me);
    }
}
