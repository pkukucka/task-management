package com.jumpsoft.taskmanagement.converter;

import com.jumpsoft.taskmanagement.enums.TaskCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TaskCategoryConverter implements AttributeConverter<TaskCategory, String> {

    @Override
    public String convertToDatabaseColumn(TaskCategory attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TaskCategory convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        
        try {
            return TaskCategory.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            // Handle invalid database values
            // You could log a warning, return a default value, or throw an exception
            return null; // Or a default value like TaskCategory.OTHER
        }
    }
}