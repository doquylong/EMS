package com.bart.test.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateQuestionRequest {
    private int level=1;
    @NotBlank
    private String content;
    @NotNull
    @NotEmpty
    private List<CreateAnswerRequest> answers = new ArrayList<>();
}
