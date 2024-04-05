package com.bart.test.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitExamRequest {
    @NotNull
    private Long questionId;
    @NotNull
    private Long selectedId;
}
