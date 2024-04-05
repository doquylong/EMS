package com.bart.test.service.impl;

import com.bart.test.config.security.JwtUtils;
import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dao.UserDao;
import com.bart.test.dto.request.CreateUserRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.dto.response.UserDetailResponse;
import com.bart.test.entity.User;
import com.bart.test.exception.AppException;
import com.bart.test.exception.ErrorInfo;
import com.bart.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.findByEmailAndDeletedIsFalse(email);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Not found email: " + email));
        User user = userOptional.get();
        return UserPrincipal.create(user);
    }

    @Override
    public AppResponse createUser(CreateUserRequest request) {
        Optional<User> userOptional = userDao.findByEmailAndDeletedIsFalse(request.getEmail());
        if (userOptional.isPresent()) throw new AppException(ErrorInfo.EMAIL_ALREADY_EXISTED);
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userDao.save(newUser);
        return AppResponse.ok(newUser.getId());
    }

    @Override
    public AppResponse getAll() {
        List<User> data = userDao.findByDeletedIsFalse();
        List<UserDetailResponse> responseData =  data.stream().map(UserDetailResponse::new).collect(Collectors.toList());
        return AppResponse.ok(responseData);
    }

    @Override
    public AppResponse getDetail(Long id) {
        Optional<User> userOptional = userDao.findByIdAndDeletedIsFalse(id);
        if (userOptional.isEmpty()) throw new AppException(ErrorInfo.NO_CONTENT);
        return AppResponse.ok(new UserDetailResponse(userOptional.get()));
    }

    @Override
    public AppResponse getProfile(UserPrincipal me) {
        Optional<User> userOptional = userDao.findByIdAndDeletedIsFalse(me.getId());
        if (userOptional.isEmpty()) throw new AppException(ErrorInfo.NO_CONTENT);
        return AppResponse.ok(new UserDetailResponse(userOptional.get()));
    }
}
