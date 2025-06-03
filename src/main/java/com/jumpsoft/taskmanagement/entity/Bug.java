package com.jumpsoft.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.jumpsoft.taskmanagement.converter.BugSeverityConverter;
import com.jumpsoft.taskmanagement.enums.BugSeverity;

/* * Bug entity represents a specific type of task that is categorized as a bug.
 * It extends the Task class and includes additional fields specific to bugs,
 * such as severity and steps to reproduce.
 *
 * This class is mapped to the database with a discriminator value of "BUG",
 * allowing it to be distinguished from other task types in a single table inheritance strategy.
 */

@Entity
@DiscriminatorValue("BUG")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Bug extends Task {

    @Convert(converter = BugSeverityConverter.class)
    @Column(name = "severity", nullable = false, length = 20)
    private BugSeverity severity;

    //TODO check
    @Column(name = "steps_to_reproduce", columnDefinition = "TEXT")
    private String stepsToReproduce;

}

