package com.bart.test.dao;

import com.bart.test.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Long> {
    List<Question> findByTestId(Long testId);
}
