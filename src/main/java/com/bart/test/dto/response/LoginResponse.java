package com.bart.test.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private Long id;
    private String token;
    private String email;
}
