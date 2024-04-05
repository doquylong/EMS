package com.bart.test.dao;

import com.bart.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestDao extends JpaRepository<Test,Long> {
    Optional<Test> findByIdAndDeletedIsFalse(Long id);
    List<Test> findByDeletedIsFalse();
    List<Test> findByDeletedIsFalseAndActiveTrue();

    Optional<Test> findByIdAndDeletedIsFalseAndActiveIs(Long id, boolean active);
}
