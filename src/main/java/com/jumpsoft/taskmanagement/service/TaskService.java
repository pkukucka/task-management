package com.jumpsoft.taskmanagement.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.NullableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.function.ThrowingConsumer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jumpsoft.taskmanagement.controller.CustomException;
import com.jumpsoft.taskmanagement.dto.TaskFilter;
import com.jumpsoft.taskmanagement.dto.error.ErrorCode;
import com.jumpsoft.taskmanagement.dto.task.BugTaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.FeatureTaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.TaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.Task;
import com.jumpsoft.taskmanagement.dto.task.TaskUpdateRequest;
import com.jumpsoft.taskmanagement.dto.task.UpdateTaskInvalidArguments;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;
import com.jumpsoft.taskmanagement.mapper.TaskMapper;
import com.jumpsoft.taskmanagement.entity.Bug;
import com.jumpsoft.taskmanagement.entity.Feature;
import com.jumpsoft.taskmanagement.entity.User;
import com.jumpsoft.taskmanagement.repository.TaskRepository;
import com.jumpsoft.taskmanagement.repository.UserRepository;
import com.jumpsoft.taskmanagement.util.TaskSpecification;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class responsible for managing tasks. Provides methods
 * for CRUD operations and task manipulation such as searching,
 * creating, updating, and deleting tasks.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;


    /**
     * Retrieves a task by its ID and converts it to a DTO representation.
     *
     * @param id The unique identifier of the task to retrieve.
     * @return An Optional containing the task DTO if found, or an empty Optional otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Task> findTaskById(Long id) {

        return taskRepository.findById(id).map(this::mapToDTO);
    }

    /**
     * Retrieves all tasks from the repository and converts them to DTO representations.
     *
     * @return A list of all task DTOs.
     */
    @Transactional(readOnly = true)
    public List<Task> findAllTasks() {
        return taskRepository.findAll()
                .stream().map(this::mapToDTO).toList();
    }


    /**
     * Searches for tasks based on a filter that supports user ID and status.
     *
     * @param filter The filter criteria to apply, containing user ID and/or status.
     * @return A list of task DTOs that match the filter criteria.
     * @throws CustomException If the user with the specified ID does not exist.
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasks(TaskFilter filter) throws CustomException {
        if (filter.getUserId() != null && !userRepository.existsById(filter.getUserId())) {
            throw new CustomException(ErrorCode.USER_WITH_ID_NOT_FOUND, String.valueOf(filter.getUserId()));
        }
        List<com.jumpsoft.taskmanagement.entity.Task> tasks = taskRepository.findAll(createStatusAndUserSpecification(filter.getStatus(), filter.getUserId()));
        return tasks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TaskCategory determineTaskCategory(Long taskId) throws EntityNotFoundException {
        com.jumpsoft.taskmanagement.entity.Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        return task.getCategory(); // Returns the TaskType enum (BUG or FEATURE)
    }

    /**
     * Creates a new task in the system based on the provided request data.
     *
     * @param taskRequest The data required to create a new task, including category and user details.
     * @return The created task converted into a DTO format.
     * @throws CustomException          If the assigned user is not found.
     * @throws IllegalArgumentException If the provided task category is invalid.
     */
    @Transactional
    public Task createTask(TaskCreateRequest taskRequest) throws CustomException {

        com.jumpsoft.taskmanagement.entity.Task task;
        User assignedUser = userRepository.findById(taskRequest.getUserId()).orElseThrow(() ->
                new CustomException(ErrorCode.USER_WITH_ID_NOT_FOUND, taskRequest.getUserId().toString())
        );

        switch (taskRequest.getCategory()) {
            case BUG:
                BugTaskCreateRequest bugRequest = (BugTaskCreateRequest) taskRequest;
                task = taskMapper.toBugEntity(bugRequest);
                break;
            case FEATURE:
                FeatureTaskCreateRequest featureRequest = (FeatureTaskCreateRequest) taskRequest;
                task = taskMapper.toFeatureEntity(featureRequest);
                break;
            default:
                throw new IllegalArgumentException("Unknown task category: " + taskRequest.getCategory());
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUser(assignedUser);
        com.jumpsoft.taskmanagement.entity.Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    /**     * Updates an existing task with the provided request data.
     *
     * @param id                  The unique identifier of the task to update.
     * @param taskRequest         The data to update the task with, including category-specific fields.
     * @param invalidRequestTypeConsumer Consumer to handle invalid arguments for specific task categories.
     * @return The updated task converted into a DTO format.
     * @throws EntityNotFoundException If the task with the given ID does not exist.
     * @throws CustomException          If the assigned user is not found or if there are validation errors.
     * @throws MethodArgumentNotValidException If the provided request data is invalid.
     */
    @Transactional
    public Task updateTask(Long id, TaskUpdateRequest taskRequest, ThrowingConsumer<UpdateTaskInvalidArguments> invalidRequestTypeConsumer) throws EntityNotFoundException, CustomException, MethodArgumentNotValidException {

        try {
            return mapToDTO(taskRepository.findById(id).map(existingTask -> {
                //check userId and update user if necessary
                User assignedUser;
                if (taskRequest.userId() != null) {
                    assignedUser = userRepository.findById(taskRequest.userId()).orElseThrow(() ->
                            new RuntimeException(new CustomException(ErrorCode.USER_WITH_ID_NOT_FOUND, taskRequest.userId().toString()))
                    );
                    if (existingTask.getUser() != null && !existingTask.getUser().getId().equals(taskRequest.userId())) {
                        existingTask.setUser(assignedUser);
                    }
                }
                if (taskRequest.name() != null) {
                    existingTask.setName(taskRequest.name());
                }
                if (taskRequest.description() != null) {
                    existingTask.setDescription(StringUtils.isEmpty(taskRequest.description()) ? null : taskRequest.description());
                }
                if (taskRequest.status() != null) {
                    existingTask.setStatus(taskRequest.status());
                }

                //check category-specific fields
                switch (existingTask.getCategory()) {
                    case BUG -> {
                        if (taskRequest.deadline() != null || taskRequest.businessValue() != null) {
                            List<String> invalidArguments = new ArrayList<>();
                            if (taskRequest.deadline() != null) {
                                invalidArguments.add("deadline");
                            }
                            if (taskRequest.businessValue() != null) {
                                invalidArguments.add("businessValue");
                            }
                            try {
                                invalidRequestTypeConsumer.acceptWithException(UpdateTaskInvalidArguments.of(existingTask.getCategory(), invalidArguments));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Bug bugTask = (Bug) existingTask;
                        if (taskRequest.severity() != null) {
                            bugTask.setSeverity(taskRequest.severity());
                        }
                        if (taskRequest.reproduceSteps() != null) {
                            bugTask.setStepsToReproduce(StringUtils.isEmpty(taskRequest.reproduceSteps()) ? null : taskRequest.reproduceSteps());
                        }
                    }
                    case FEATURE -> {
                        if (taskRequest.reproduceSteps() != null || taskRequest.severity() != null) {
                            List<String> invalidArguments = new ArrayList<>();
                            if (taskRequest.reproduceSteps() != null) {
                                invalidArguments.add("reproduceSteps");
                            }
                            if (taskRequest.severity() != null) {
                                invalidArguments.add("severity");
                            }
                            try {
                                invalidRequestTypeConsumer.acceptWithException(UpdateTaskInvalidArguments.of(existingTask.getCategory(), invalidArguments));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Feature featureTask = (Feature) existingTask;
                        if (taskRequest.businessValue() != null) {
                            featureTask.setBusinessValue(StringUtils.isEmpty(taskRequest.businessValue()) ? null : taskRequest.businessValue());
                        }
                        if (taskRequest.deadline() != null) {
                            featureTask.setDeadline(taskRequest.deadline());
                        }
                    }
                    default ->
                            throw new IllegalArgumentException("Unknown task category: " + existingTask.getCategory());
                }
                return taskRepository.save(existingTask);
            }).orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found")));
        } catch (RuntimeException e) {
            switch (e.getCause()){
                case CustomException customException -> {
                    throw customException;
                }
                case MethodArgumentNotValidException methodArgumentNotValidException -> {
                    throw methodArgumentNotValidException;
                }
                default -> {
                    throw e;
                }
            }
        }

    }

    /**
     * Deletes a task from the system by its unique identifier.
     *
     * @param id The unique identifier of the task to delete.
     * @throws EntityNotFoundException If the task with the given ID does not exist.
     */
    @Transactional
    public void deleteTask(Long id) throws EntityNotFoundException {
        // Check if task exists first
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    private Task mapToDTO(com.jumpsoft.taskmanagement.entity.Task task) {
        return switch (task.getCategory()) {
            case BUG -> taskMapper.toBugDTO((Bug) task);
            case FEATURE -> taskMapper.toFeatureDTO((Feature) task);
            default -> throw new IllegalArgumentException("Unknown task category: " + task.getCategory());
        };
    }

    private Specification<com.jumpsoft.taskmanagement.entity.Task> createStatusAndUserSpecification(TaskStatus status, Long userId) {

        Specification<com.jumpsoft.taskmanagement.entity.Task> spec = Specification.where(null);
        if (status != null) {
            spec = spec.and(TaskSpecification.withStatus(status));
        }
        if (userId != null) {
            spec = spec.and(TaskSpecification.withUserId(userId));
        }
        return spec;
    }

}