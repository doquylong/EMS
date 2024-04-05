package com.bart.test.dao;

import com.bart.test.entity.Role;
import com.bart.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    Optional<User> findByEmailAndDeletedIsFalse (String email);
    List<User> findByRoleAndDeletedIsFalse (Role role);

    Optional<User> findByIdAndDeletedIsFalse (Long id);
    List<User> findByDeletedIsFalse ();
}
