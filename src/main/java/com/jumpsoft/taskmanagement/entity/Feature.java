package com.jumpsoft.taskmanagement.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* * Feature entity represents a specific type of task that is categorized as a feature.
 * It extends the Task class and includes additional fields specific to features,
 * such as business value and deadline.
 *
 * This class is mapped to the database with a discriminator value of "FEATURE",
 * allowing it to be distinguished from other task types in a single table inheritance strategy.
 */

@Entity
@DiscriminatorValue("FEATURE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Feature extends Task {

    @Column(name = "business_value", nullable = false, length = 500)
    private String businessValue;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

}

