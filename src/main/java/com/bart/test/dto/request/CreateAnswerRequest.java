package com.bart.test.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAnswerRequest {
    @NotBlank
    private String content;
    @NotNull
    private Boolean correct;
}
