package com.jumpsoft.taskmanagement.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import com.jumpsoft.taskmanagement.converter.TaskCategoryConverter;
import com.jumpsoft.taskmanagement.converter.TaskStatusConverter;

import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

/* * Task entity represents a generic task in the task management system.
 * It serves as a base class for specific types of tasks, such as Feature and Bug.
 *
 * This class is mapped to the database with a single table inheritance strategy,
 * allowing different task types to be stored in the same table with a discriminator column.
 */

@Entity
@Table(name = "tasks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category", discriminatorType = DiscriminatorType.STRING, length = 10)
@Setter
@Getter
@SequenceGenerator(name = "id_generator", sequenceName = "task_id_seq", allocationSize = 1)
public class Task extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Convert(converter = TaskCategoryConverter.class)
    @Column(name = "category", insertable = false, updatable = false)
    private TaskCategory category;

    @Convert(converter = TaskStatusConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;
}
