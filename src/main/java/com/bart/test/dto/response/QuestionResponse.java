package com.bart.test.dto.response;

import com.bart.test.entity.Question;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionResponse {
    private Long id;
    private String content;
    private List<AnswerResponse> answers;

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.content = question.getContent();
        this.answers = question.getAnswers().stream().map(AnswerResponse::new).collect(Collectors.toList());
    }
}
