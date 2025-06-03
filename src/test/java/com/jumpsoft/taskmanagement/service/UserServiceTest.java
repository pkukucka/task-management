package com.jumpsoft.taskmanagement.service;

import com.jumpsoft.taskmanagement.controller.CustomException;
import com.jumpsoft.taskmanagement.dto.error.ErrorCode;
import com.jumpsoft.taskmanagement.dto.user.UserCreateRequest;
import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.dto.user.UserUpdateRequest;
import com.jumpsoft.taskmanagement.mapper.UserMapper;
import com.jumpsoft.taskmanagement.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for UserService.
 * Tests all methods: findUserById, findAllUsers, createUser, updateUser, deleteUser.
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({UserService.class, UserMapper.class})
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserCreateRequest testUserCreateRequest;
    private UserCreateRequest testUserCreateRequestDuplicateUsername;
    private com.jumpsoft.taskmanagement.entity.User persistedUser;

    @Before
    public void setUp() {
        // Clear any existing data
        userRepository.deleteAll();
        
        // Create and persist a test user
        com.jumpsoft.taskmanagement.entity.User user = new com.jumpsoft.taskmanagement.entity.User();
        user.setUsername("testuser");
        user.setFullName("Test User");
        
        persistedUser = entityManager.persistAndFlush(user);
        
        // Create a UserCreateRequest for testing
        testUserCreateRequest = new UserCreateRequest("newuser", "New User");
        testUserCreateRequestDuplicateUsername = new UserCreateRequest("newuser", "New User1");
    }

    @Test
    public void findUserById_WhenUserExists_ReturnsUserDTO() {
        // Act
        Optional<User> foundUser = userService.findUserById(persistedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(persistedUser.getId(), foundUser.get().id());
        assertEquals(persistedUser.getUsername(), foundUser.get().username());
        assertEquals(persistedUser.getFullName(), foundUser.get().fullName());
    }

    @Test
    public void findUserById_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Optional<User> notFoundUser = userService.findUserById(nonExistentId);
        assertFalse(notFoundUser.isPresent());
    }

    @Test
    public void findAllUsers_ReturnsListOfUserDTOs() {
        // Arrange
        com.jumpsoft.taskmanagement.entity.User secondUser = new com.jumpsoft.taskmanagement.entity.User();
        secondUser.setUsername("seconduser");
        secondUser.setFullName("Second User");
        entityManager.persistAndFlush(secondUser);

        // Act
        List<User> users = userService.findAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        
        // Verify both users are in the result
        boolean foundUser1 = false;
        boolean foundUser2 = false;
        
        for (User userDTO : users) {
            if (userDTO.username().equals(persistedUser.getUsername())) {
                foundUser1 = true;
            }
            if (userDTO.username().equals(secondUser.getUsername())) {
                foundUser2 = true;
            }
        }
        
        assertTrue("First user found in results", foundUser1);
        assertTrue("Second user found in results", foundUser2);
    }

    @Test
    public void createUser_ReturnsCreatedUserDTO() throws CustomException {
        // Act
        User createdUser = userService.createUser(testUserCreateRequest);

        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.id());
        assertEquals(testUserCreateRequest.username(), createdUser.username());
        assertEquals(testUserCreateRequest.fullName(), createdUser.fullName());

        // Verify it's in the database
        com.jumpsoft.taskmanagement.entity.User dbUser = entityManager.find(com.jumpsoft.taskmanagement.entity.User.class, createdUser.id());
        assertNotNull(dbUser);
        assertEquals(testUserCreateRequest.username(), dbUser.getUsername());
    }

    @Test
    public void createUser_WhenUsernameExists_ThrowsCustomException() throws CustomException {

        // Given
        UserCreateRequest duplicateRequest = testUserCreateRequestDuplicateUsername;

        String expectedErrorMessage = MessageFormat.format(ErrorCode.DUPLICATE_USERNAME.getMessage(), duplicateRequest.username());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.createUser(duplicateRequest);
        });

        // Additional assertions
        assertEquals(ErrorCode.DUPLICATE_USERNAME, exception.getErrorCode());
        assertEquals(expectedErrorMessage, MessageFormat.format(exception.getErrorCode().getMessage(), exception.getData()) );
    }

    @Test
    public void updateUser_WhenUserExists_ReturnsUpdatedUserDTO() {
        // Arrange
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("Updated Name");

        // Act
        User updatedUser = userService.updateUser(persistedUser.getId(), userUpdateRequest);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(persistedUser.getId(), updatedUser.id());
        assertEquals(userUpdateRequest.fullName(), updatedUser.fullName());
        
        // Verify it's updated in the database
        com.jumpsoft.taskmanagement.entity.User dbUser = entityManager.find(com.jumpsoft.taskmanagement.entity.User.class, persistedUser.getId());
        assertEquals(userUpdateRequest.fullName(), dbUser.getFullName());
    }

    @Test
    public void updateUser_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 999L;
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("Updated Name1");
        String expectedErrorMessage = MessageFormat.format(ErrorCode.USER_WITH_ID_NOT_FOUND.getMessage(), nonExistentId);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUser(nonExistentId, userUpdateRequest);
        });

        // Additional assertions
        assertEquals(ErrorCode.USER_WITH_ID_NOT_FOUND, exception.getErrorCode());
        assertEquals(expectedErrorMessage,
                MessageFormat.format(exception.getErrorCode().getMessage(), exception.getData()));
    }

    @Test
    public void deleteUser_WhenUserExists_DeletesUser() {
        // Act
        userService.deleteUser(persistedUser.getId());

        // Assert
        assertFalse(userRepository.existsById(persistedUser.getId()));
    }

    @Test
    public void deleteUser_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 999L;
        String expectedErrorMessage = MessageFormat.format(ErrorCode.USER_WITH_ID_NOT_FOUND.getMessage(), nonExistentId);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.deleteUser(nonExistentId);
        });

        // Additional assertions
        assertEquals(ErrorCode.USER_WITH_ID_NOT_FOUND, exception.getErrorCode());
        assertEquals(expectedErrorMessage,
                MessageFormat.format(exception.getErrorCode().getMessage(), exception.getData()));

    }

}