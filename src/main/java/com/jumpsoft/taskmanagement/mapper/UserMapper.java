package com.jumpsoft.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jumpsoft.taskmanagement.dto.user.UserCreateRequest;
import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.dto.user.UserUpdateRequest;

/**
 * Mapper interface for converting between User DTOs and User entities.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  User toDTO(com.jumpsoft.taskmanagement.entity.User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tasks", ignore = true)
  com.jumpsoft.taskmanagement.entity.User toEntity(UserCreateRequest dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "tasks", ignore = true)
  com.jumpsoft.taskmanagement.entity.User toEntity(UserUpdateRequest dto);
}