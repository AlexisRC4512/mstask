package com.project.mstask.entity;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskEntityTest {
    @Test
    void testBuilder() {
        Task task = Task.builder()
                .taskID(1)
                .name("Sample Task")
                .projectID(1)
                .description("Description")
                .startDate(new Timestamp(System.currentTimeMillis()))
                .endDate(new Timestamp(System.currentTimeMillis()))
                .state(1)
                .percentageComplete(50)
                .percentageProject(100)
                .fullNameMember("John Doe")
                .fullNameLeader("Jane Doe")
                .createdBy("John")
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .modifiedBy("Jane")
                .dateModified(new Timestamp(System.currentTimeMillis()))
                .deletedBy("Admin")
                .dateDeleted(new Timestamp(System.currentTimeMillis()))
                .build();

        assertEquals(1, task.getTaskID());
        assertEquals("Sample Task", task.getName());
        assertEquals(1, task.getProjectID());
        assertEquals("Description", task.getDescription());

    }

    @Test
    void testBuilderWithDefaults() {
        Task task = Task.builder()
                .taskID(1)
                .name("Sample Task")
                .projectID(1)
                .startDate(new Timestamp(System.currentTimeMillis()))
                .endDate(new Timestamp(System.currentTimeMillis()))
                .build();

        assertEquals(1, task.getTaskID());
        assertEquals("Sample Task", task.getName());
        assertEquals(1, task.getProjectID());

    }
}
