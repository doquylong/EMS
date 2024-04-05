package com.bart.test.dao;

import com.bart.test.dto.response.AnswerResponse;
import com.bart.test.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AnswerDao extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdInAndCorrectIsTrue(Set<Long> questionIds);
}
