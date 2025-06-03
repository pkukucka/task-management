package com.jumpsoft.taskmanagement.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jumpsoft.taskmanagement.controller.CustomException;
import com.jumpsoft.taskmanagement.dto.TaskFilter;
import com.jumpsoft.taskmanagement.dto.error.ErrorCode;
import com.jumpsoft.taskmanagement.dto.task.BugTaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.BugTaskUpdateRequest;
import com.jumpsoft.taskmanagement.dto.task.FeatureTaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.FeatureTaskUpdateRequest;
import com.jumpsoft.taskmanagement.dto.task.TaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.Task;
import com.jumpsoft.taskmanagement.dto.task.TaskUpdateRequest;
import com.jumpsoft.taskmanagement.enums.TaskStatus;
import com.jumpsoft.taskmanagement.mapper.TaskMapper;
import com.jumpsoft.taskmanagement.entity.Bug;
import com.jumpsoft.taskmanagement.entity.Feature;
import com.jumpsoft.taskmanagement.entity.User;
import com.jumpsoft.taskmanagement.repository.TaskRepository;
import com.jumpsoft.taskmanagement.repository.UserRepository;

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

    /**
     * Updates an existing task with the provided request data.
     *
     * @param id          The unique identifier of the task to update.
     * @param taskRequest The data required to update the task, including optional fields such as name and description.
     * @return The updated task as a DTO.
     * @throws EntityNotFoundException If the task with the given ID does not exist.
     * @throws CustomException         If the assigned user is not found or validation fails.
     */
    @Transactional
    public Task updateTask(Long id, TaskUpdateRequest taskRequest) throws EntityNotFoundException, CustomException {

        return mapToDTO(taskRepository.findById(id).map(existingTask -> {
           User assignedUser;
           if (taskRequest.getUserId() != null) {
               assignedUser = userRepository.findById(taskRequest.getUserId()).orElseThrow(() ->
                       new RuntimeException(new CustomException(ErrorCode.USER_WITH_ID_NOT_FOUND, taskRequest.getUserId().toString()))
               );
               if (existingTask.getUser() != null && !existingTask.getUser().getId().equals(taskRequest.getUserId())) {
                     existingTask.setUser(assignedUser);
               }
           }
           if (taskRequest.getName() != null) {
               existingTask.setName(taskRequest.getName());
           }
           if (taskRequest.getDescription() != null) {
               existingTask.setDescription(StringUtils.isEmpty(taskRequest.getDescription()) ? null : taskRequest.getDescription());
           }
           if (taskRequest.getStatus() != null) {
               existingTask.setStatus(taskRequest.getStatus());
           }

           switch (existingTask.getCategory()) {
                case BUG -> {
                    if(taskRequest instanceof BugTaskUpdateRequest) {
                        BugTaskUpdateRequest bugRequest = (BugTaskUpdateRequest) taskRequest;
                        Bug bugTask = (Bug) existingTask;
                        if (bugRequest.severity() != null) {
                            bugTask.setSeverity(bugRequest.severity());
                        }
                        if (bugRequest.reproduceSteps() != null) {
                            bugTask.setStepsToReproduce(StringUtils.isEmpty(bugRequest.reproduceSteps()) ? null : bugRequest.reproduceSteps());
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid request type for BUG category");
                    }
                }
                case FEATURE -> {
                    if (taskRequest instanceof FeatureTaskUpdateRequest) {
                        FeatureTaskUpdateRequest featureRequest = (FeatureTaskUpdateRequest) taskRequest;
                        Feature featureTask = (Feature) existingTask;
                        if (featureRequest.businessValue() != null) {
                            featureTask.setBusinessValue(StringUtils.isEmpty(featureRequest.businessValue()) ? null : featureRequest.businessValue());
                        }
                        if (featureRequest.deadline() != null) {
                            featureTask.setDeadline(featureRequest.deadline());
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid request type for FEATURE category");
                    }

                }
                default -> throw new IllegalArgumentException("Unknown task category: " + existingTask.getCategory());
            }
            return taskRepository.save(existingTask);
        }
        ).orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found")));

    }

    /**
     * Deletes a task from the system by its unique identifier.
     *
     * @param id The unique identifier of the task to delete.
     * @throws EntityNotFoundException If the task with the given ID does not exist.
     */
    @Transactional
    public void deleteTask(Long id) throws EntityNotFoundException {
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
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("status"), status),
                        criteriaBuilder.equal(root.get("user").get("id"), userId)
                );
    }


}