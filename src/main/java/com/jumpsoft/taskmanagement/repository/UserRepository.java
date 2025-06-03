package com.jumpsoft.taskmanagement.repository;

import java.util.Optional;

import com.jumpsoft.taskmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query methods
    Optional<User> findByUsername(String username);
}