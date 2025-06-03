package com.jumpsoft.taskmanagement.dto.task;

import java.util.List;

import com.jumpsoft.taskmanagement.enums.TaskCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents invalid arguments for updating a task.
 * This class is used to encapsulate the category of the task and the names of the invalid arguments.
 */
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UpdateTaskInvalidArguments {

    private TaskCategory category;
    //list
    private List<String> argNames;

    public static UpdateTaskInvalidArguments of(TaskCategory category, List<String> argNames) {
        return new UpdateTaskInvalidArguments(category, argNames);
    }
}
