package com.jumpsoft.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jumpsoft.taskmanagement.dto.task.BugTask;
import com.jumpsoft.taskmanagement.dto.task.BugTaskCreateRequest;
import com.jumpsoft.taskmanagement.dto.task.FeatureTask;
import com.jumpsoft.taskmanagement.dto.task.FeatureTaskCreateRequest;
import com.jumpsoft.taskmanagement.entity.Bug;
import com.jumpsoft.taskmanagement.entity.Feature;

/**
 * Mapper interface for converting between Task DTOs and Task entities.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TaskMapper {

    BugTask toBugDTO(Bug bug);

    FeatureTask toFeatureDTO(Feature feature);

    @Mapping(target = "id", ignore = true)
    Bug toBugEntity(BugTaskCreateRequest bugRequest);

    @Mapping(target = "id", ignore = true)
    Feature toFeatureEntity(FeatureTaskCreateRequest featureRequest);

}