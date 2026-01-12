package com.ali_b1812.app.model.enums;

public enum TaskStatus {
    PENDING,        // Created but not started
    IN_PROGRESS,    // Currently being worked on
    COMPLETED,      // Finished successfully
    CANCELLED,      // Cancelled before completion
    BLOCKED         // Cannot proceed (waiting)
}
