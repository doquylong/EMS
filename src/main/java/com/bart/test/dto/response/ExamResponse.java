package com.bart.test.dto.response;

import com.bart.test.entity.Exam;
import com.bart.test.entity.ExamStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamResponse {
    private String performedBy;
    private int level;
    private String title;
    private float score;
    private ExamStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime actualFinishedAt;

    public ExamResponse(Exam exam) {
        this.performedBy = exam.getStudent().getName();
        this.level = exam.getTest().getLevel();
        this.title = exam.getTest().getTitle();
        this.score = exam.getScore();
        this.status = exam.getStatus();
        this.startedAt = exam.getStartedAt();
        this.finishedAt = exam.getFinishedAt();
        this.actualFinishedAt = exam.getActualFinishedAt();
    }
}
