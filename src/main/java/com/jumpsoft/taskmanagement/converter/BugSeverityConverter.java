package com.jumpsoft.taskmanagement.converter;

import  com.jumpsoft.taskmanagement.enums.BugSeverity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class BugSeverityConverter implements AttributeConverter<BugSeverity, String> {

    @Override
    public String convertToDatabaseColumn(BugSeverity attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public BugSeverity convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        
        try {
            return BugSeverity.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}