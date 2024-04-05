package com.bart.test.dao;

import com.bart.test.entity.ExamResult;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultDao extends JpaRepository<ExamResult,Long> {
}
