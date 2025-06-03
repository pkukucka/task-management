package com.jumpsoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the task management system.
 * Each user can have multiple tasks assigned to them.
 * The username must be unique and is used for identification.
 */
@Entity
@Table(name = "users")
@Setter
@Getter
@SequenceGenerator(name = "id_generator", sequenceName = "user_id_seq", allocationSize = 1)
public class User extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "full_name", nullable = false, length = 40)
    private String fullName;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    /**
     * Adds a task to this user's list of assigned tasks.
     * Initializes the tasks list if it's null.
     *
     * @param task The task to be added to this user
     */
    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        task.setUser(this);
    }

}