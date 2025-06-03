package com.jumpsoft.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for the Task Management application.
 * This class is responsible for bootstrapping the Spring Boot application.
 * It enables transaction management for database operations.
 */
@SpringBootApplication
@EnableTransactionManagement
public class TaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }
}
