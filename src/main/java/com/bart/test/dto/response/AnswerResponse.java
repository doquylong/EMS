package com.bart.test.dto.response;

import com.bart.test.entity.Answer;
import lombok.Data;

@Data
public class AnswerResponse {
    private Long id;
    private String content;

    public AnswerResponse(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
    }
}
