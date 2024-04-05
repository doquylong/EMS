package com.bart.test.dto.response;

import com.bart.test.entity.Test;
import lombok.Data;

@Data
public class TestResponse {
    private Long id;
    private String title;
    private int level;
    private int time;
    private boolean active;

    public TestResponse(Test test) {
        this.id = test.getId();
        this.title = test.getTitle();
        this.level = test.getLevel();
        this.time = test.getTime();
        this.active = test.isActive();
    }
}
