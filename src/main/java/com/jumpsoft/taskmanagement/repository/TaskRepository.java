package com.jumpsoft.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jumpsoft.taskmanagement.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>
{
    // This interface will automatically provide CRUD operations and support for JPA specifications.
    // Additional custom query methods can be defined here if needed.
}
