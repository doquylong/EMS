package com.bart.test.config;

import com.bart.test.dao.UserDao;
import com.bart.test.entity.Role;
import com.bart.test.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitRunnerConfig implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;

    private final UserDao userDao;


    @Override
    public void run(String... args) {
        List<User> userList = userDao.findByRoleAndDeletedIsFalse(Role.TEACHER);
        if (userList.isEmpty()){
            User user = User.builder()
                    .name("Teacher 1")
                    .role(Role.TEACHER)
                    .password(passwordEncoder.encode("12345678"))
                    .email("teacher@example.com")
                    .build();
            userDao.save(user);
            log.info("Created teacher user");
        }
    }
}
