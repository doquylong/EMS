package com.bart.test.dao;

import com.bart.test.entity.Exam;
import com.bart.test.entity.ExamStatus;
import com.bart.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamDao extends JpaRepository<Exam,Long> {
    Optional<Exam> findByIdAndStatus(Long id, ExamStatus status);
    List<Exam> findAllByStudent(User user);
}
