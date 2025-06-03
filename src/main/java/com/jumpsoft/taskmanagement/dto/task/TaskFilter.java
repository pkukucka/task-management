package com.jumpsoft.taskmanagement.dto;

import com.jumpsoft.taskmanagement.enums.TaskStatus;
import com.jumpsoft.taskmanagement.validation.ValidTaskFilter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter criteria for Task queries.
 * Encapsulates parameters used for filtering tasks in search operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidTaskFilter
public class TaskFilter {
    
    /**
     * The status of the task to filter by.
     * Can be null if no status filtering is needed.
     */
    private TaskStatus status;
    
    /**
     * The ID of the user associated with the task.
     * Can be null if no user filtering is needed.
     */
    private Long userId;
    
    /**
     * Checks if this filter has any criteria set.
     * 
     * @return true if at least one filter criterion is set, false otherwise
     */
    public boolean hasAnyCriteria() {
        return status != null || userId != null;
    }
}