package com.project.mstask.controller;

import com.project.mstask.aggregates.constants.Constant;
import com.project.mstask.aggregates.response.BaseResponse;
import com.project.mstask.entity.Task;
import com.project.mstask.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;
    private Task task;
    @BeforeEach
    void setUp()
    {
        task=Task.builder()
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
    }
    @Test
    void testGetTaskById_WhenAuthorized() {
        when(taskService.validateToken(anyString())).thenReturn(true);
        when(taskService.getTaskForId(1l)).thenReturn(new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS,Optional.of(task)));
        BaseResponse response = taskController.getTaskById(1L, "dummyToken");
        assertEquals(Constant.CODE_SUCCESS, response.getCode());
    }

    @Test
    void testGetTaskById_WhenNotAuthorized() {
        when(taskService.validateToken(anyString())).thenReturn(false);
        BaseResponse response = taskController.getTaskById(2L, "dummyToken");
        assertEquals(Constant.CODE_NOT_AUTHORIZED, response.getCode());
    }
}