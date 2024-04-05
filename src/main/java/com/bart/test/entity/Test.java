package com.bart.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int level;
    private int time;
    private boolean active;

    @OneToMany(mappedBy = "test")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "test")
    List<Exam> exams;
}
