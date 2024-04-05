package com.bart.test.dto.response;

import com.bart.test.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Date createdAt;
    public UserDetailResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }
}
