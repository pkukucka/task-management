package com.jumpsoft.taskmanagement.controller;

import com.jumpsoft.taskmanagement.dto.TaskFilter;
import com.jumpsoft.taskmanagement.dto.task.Task;
import com.jumpsoft.taskmanagement.dto.task.TaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.TaskUpdateRequest;
import com.jumpsoft.taskmanagement.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * TaskController provides RESTful endpoints for managing tasks in the system.
 * It allows for creating, retrieving, updating, and deleting tasks.
 */

@RestController
@RequestMapping("/api/tasks")
@Validated
@Tag(name = "Tasks", description = "API for managing tasks in the system.")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of tasks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Fetch a task based on its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Unique identifier of the task", example = "1", required = true)
            @PathVariable("id") Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task by providing the required task details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or missing fields"),
            @ApiResponse(responseCode = "404", description = "User with the specified userId not found"),})
    public ResponseEntity<Task> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task information for creation", required = true,
                    content = @Content(schema = @Schema(implementation = TaskCreateRequest.class)))
            @Valid @RequestBody TaskCreateRequest taskRequest) throws CustomException {
        Task createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PostMapping("/search")
    @Operation(summary = "Search tasks by complex filter", description = "Search for tasks based on a variety of filtering criteria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the filtered list of tasks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter data provided"),
            @ApiResponse(responseCode = "404", description = "User with the specified userId not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Task>> searchTasksByFilter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Filter parameters for task search", required = true,
                    content = @Content(schema = @Schema(implementation = TaskFilter.class)))
            @Valid @NotNull @RequestBody TaskFilter filter)  throws CustomException {
        return ResponseEntity.ok(taskService.searchTasks(filter));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Update the details of an existing task by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid task data or invalid request body"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "404", description = "User with the specified userId not found"),
    })
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Unique identifier of the task to be updated", example = "1", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task information for update", required = true,
                    content = @Content(schema = @Schema(implementation = TaskUpdateRequest.class)))
            @Valid @RequestBody TaskUpdateRequest taskRequest) throws CustomException {
        Task updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a task based on its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Unique identifier of the task to be deleted", example = "1", required = true)
            @PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}