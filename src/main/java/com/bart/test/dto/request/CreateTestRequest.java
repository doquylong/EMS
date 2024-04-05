package com.bart.test.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTestRequest {
    @NotBlank
    private String title;
    @NotNull
    private int level;
    @NotNull
    private int time;
}
