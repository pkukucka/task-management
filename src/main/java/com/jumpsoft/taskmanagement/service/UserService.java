package com.jumpsoft.taskmanagement.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.jumpsoft.taskmanagement.controller.CustomException;
import com.jumpsoft.taskmanagement.dto.error.ErrorCode;
import com.jumpsoft.taskmanagement.dto.user.UserCreateRequest;
import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.dto.user.UserUpdateRequest;
import com.jumpsoft.taskmanagement.mapper.UserMapper;
import com.jumpsoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class responsible for managing users. Provides methods
 * for CRUD operations and user manipulation such as searching,
 * creating, updating, and deleting users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the found User, or an empty Optional if not found.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {

        return userRepository.findById(id).map(userMapper::toDTO);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list containing all users mapped to DTOs.
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {

        return userRepository.findAll()
                .stream().map(userMapper::toDTO).toList();
    }

    /**
     * Creates a new user in the system.
     *
     * @param userRequest The request containing user creation details.
     * @return The created User object mapped to a DTO.
     * @throws CustomException If a user with the provided username already exists.
     */
    @Transactional
    public User createUser(UserCreateRequest userRequest) throws CustomException {

        if (userRepository.findByUsername(userRequest.username()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME, userRequest.username());
        }
        return userMapper.toDTO(userRepository.save(userMapper.toEntity(userRequest)));
    }

    /**
     * Updates an existing user's details.
     *
     * @param id          The ID of the user to update.
     * @param userRequest The request containing the updated user details.
     * @return The updated User object mapped to a DTO.
     * @throws EntityNotFoundException If no user with the provided ID is found.
     */
    @Transactional
    public User updateUser(Long id, UserUpdateRequest userRequest) throws EntityNotFoundException {

        return userMapper.toDTO(userRepository.findById(id)
                .map(existingUser -> {
                    if (StringUtils.isNoneBlank(userRequest.fullName())) {
                        existingUser.setFullName(userRequest.fullName());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found")));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
