package com.project.mstask.service;

import com.project.mstask.aggregates.constants.Constant;
import com.project.mstask.aggregates.request.TaskRequest;
import com.project.mstask.aggregates.request.UpdateTaskRequest;
import com.project.mstask.aggregates.response.BaseResponse;
import com.project.mstask.aggregates.response.ProjectResponse;
import com.project.mstask.aggregates.response.ProjectUserResponse;
import com.project.mstask.entity.Task;
import com.project.mstask.feingclient.ProjectUserClient;
import com.project.mstask.feingclient.UserClient;
import com.project.mstask.repositoy.TaskRepository;
import com.project.mstask.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private  UserClient userClient;
    @Mock
    private  ProjectUserClient projectUserClient;
    @InjectMocks
    private TaskServiceImpl taskService;
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
    void testValidateToken() {
        String token = "token";
        when(userClient.validateApiToken(token)).thenReturn(true);
        Boolean result = taskService.validateToken(token);
        assertTrue(result);
        verify(userClient).validateApiToken(token);
    }

    @Test
    void testValidateTokenWithEmptyToken() {
        String token = "";
        Boolean result = taskService.validateToken(token);
        assertThat(result).isFalse();
        verify(userClient, never()).validateApiToken(anyString());
    }

    @Test
    void testValidateTokenWithException() {
        String token = "tokeninvalido";
        when(userClient.validateApiToken(token)).thenThrow(new RuntimeException());
        Boolean result = taskService.validateToken(token);
        assertThat(result).isFalse();
        verify(userClient).validateApiToken(token);
    }

    @Test
    void getTaskForId()
    {
        Long id=1L;
        given(taskRepository.findById(id)).willReturn(Optional.of(task));
        Task task1= (Task) taskService.getTaskForId((long) task.getTaskID()).getData().get();
        BaseResponse baseResponse= new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS,Optional.of(task));
        assertThat(baseResponse.getData().get()).isNotNull();
    }
    @Test
    void getTaskForIdNull() {
        Long id = 2L;
        given(taskRepository.findById(id)).willReturn(Optional.empty());
        BaseResponse baseResponse = taskService.getTaskForId(id);
        assertFalse(baseResponse.getData().isPresent());
        assertEquals(Constant.CODE_ERROR_NULL, baseResponse.getCode());
    }
    @Test
    void testCreateTask_Success() {
        Long idLeader = 1L;
        Long idProject = 1L;
        String emailMember = "example@example.com";

        ProjectUserResponse leaderCreate = ProjectUserResponse.builder()
                .fullName("John Doe")
                .leaderId(1L)
                .project(ProjectResponse.builder().projectId(1L).build())
                .build();

        ProjectUserResponse memberCreate = ProjectUserResponse.builder()
                .fullName("Jane Doe")
                .memberId(2L)
                .build();

        Task task = Task.builder()
                .name("Test Task")
                .description("This is a test task")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(7)))
                .projectID(1)
                .percentageProject(100)
                .build();

        when(projectUserClient.getProjectUser(idLeader)).thenReturn(leaderCreate);
        when(projectUserClient.getProjectMember(emailMember)).thenReturn(memberCreate);
        BaseResponse response = taskService.createTask(idLeader, idProject, task, emailMember);
        Assertions.assertEquals(Constant.CODE_SUCCESS, response.getCode());
        Assertions.assertEquals(Constant.MESS_SUCCESS, response.getMessage());
        Assertions.assertTrue(response.getData().isPresent());
        Assertions.assertEquals(task.getName(), ((Task) response.getData().get()).getName());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_NullLeader() {
        Long idLeader = 1L;
        Long idProject = 1L;
        String emailMember = "example@example.com";
        when(projectUserClient.getProjectUser(idLeader)).thenReturn(null);
        BaseResponse response = taskService.createTask(idLeader, idProject, new Task(), emailMember);
        Assertions.assertEquals(Constant.CODE_ERROR_NULL, response.getCode());
        Assertions.assertEquals(Constant.MESS_NULL, response.getMessage());
        Assertions.assertFalse(response.getData().isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testUpdateTask_Success() {
        Long idLeader = 1L;
        Long idTask = 1L;

        ProjectUserResponse leader = ProjectUserResponse.builder()
                .fullName("John Doe")
                .build();

        TaskRequest taskRequest = TaskRequest.builder()
                .name("Updated Task Name")
                .description("Updated Task Description")
                .fullNameLeader("Updated Leader")
                .fullNameMember("Updated Member")
                .leaderID(2)
                .memberID(3)
                .state(1)
                .build();

        Task existingTask = Task.builder()
                .name("Old Task Name")
                .description("Old Task Description")
                .build();

        when(projectUserClient.getProjectUser(idLeader)).thenReturn(leader);
        when(taskRepository.findById(idTask)).thenReturn(Optional.of(existingTask));
        BaseResponse response = taskService.updateTask(idLeader, idTask, taskRequest);
        Assertions.assertEquals(Constant.CODE_SUCCESS, response.getCode());
        Assertions.assertEquals(Constant.MESS_SUCCESS, response.getMessage());
        Assertions.assertTrue(response.getData().isPresent());
        Assertions.assertEquals(taskRequest.getName(), ((Task) response.getData().get()).getName());
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    void testUpdatePercentage_Success() {
        Long idMember = 1L;
        Long taskId = 1L;
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(50);
        ProjectUserResponse member = ProjectUserResponse.builder()
                .fullName("Jane Doe")
                .project(ProjectResponse.builder().projectId(1L).percentageComplete(50).build())
                .build();
        when(projectUserClient.getProjectMemberId(idMember)).thenReturn(member);
        Task task = Task.builder()
                .taskID(1)
                .name("Sample Task")
                .projectID(123)
                .description("Task description")
                .startDate(Timestamp.valueOf("2023-01-01 10:00:00"))
                .endDate(Timestamp.valueOf("2023-01-10 18:00:00"))
                .state(1)
                .percentageComplete(50)
                .percentageProject(20)
                .memberID(456)
                .leaderID(789)
                .fullNameMember("Jane Doe")
                .fullNameLeader("John Smith")
                .createdBy("Admin")
                .dateCreated(Timestamp.valueOf("2023-01-01 12:00:00"))
                .build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        BaseResponse response = taskService.updatePercentage(idMember, taskId, updateTaskRequest, "token");
        assertEquals(Constant.CODE_SUCCESS, response.getCode());
        assertEquals(Constant.MESS_SUCCESS, response.getMessage());
        assertTrue(response.getData().isPresent());
        Task updatedTask = (Task) response.getData().get();
        assertEquals(100, updatedTask.getPercentageComplete());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
     void testDeleteTask_Success() {
        Long idLeader = 1L;
        Long idTask = 1L;
        ProjectUserResponse leader = ProjectUserResponse.builder()
                .fullName("John Doe")
                .build();
        Task task = Task.builder()
                .name("Task to Delete")
                .build();
        when(projectUserClient.getProjectUser(idLeader)).thenReturn(leader);
        when(taskRepository.findById(idTask)).thenReturn(Optional.of(task));
        BaseResponse response = taskService.deleteTask(idLeader, idTask);
        Assertions.assertEquals(Constant.CODE_SUCCESS, response.getCode());
        Assertions.assertEquals(Constant.MESS_SUCCESS, response.getMessage());
        Assertions.assertTrue(response.getData().isPresent());
        Assertions.assertEquals("Task to Delete", ((Task) response.getData().get()).getName());
        Assertions.assertEquals(0, ((Task) response.getData().get()).getState());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void testUpdateTask_LeaderNull() {
        Long idLeader = 1L;
        Long idTask = 1L;
        TaskRequest taskRequest = new TaskRequest();
        when(projectUserClient.getProjectUser(idLeader)).thenReturn(null);
        BaseResponse response = taskService.updateTask(idLeader, idTask, taskRequest);
        Assertions.assertEquals(Constant.CODE_ERROR, response.getCode());
        Assertions.assertTrue(response.getMessage().contains("The leader must not be null and the projectid does not match or the email is null"));
        Assertions.assertFalse(response.getData().isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        Long idLeader = 1L;
        Long idTask = 1L;
        TaskRequest taskRequest = new TaskRequest();
        ProjectUserResponse leader = ProjectUserResponse.builder()
                .fullName("John Doe")
                .build();
        when(projectUserClient.getProjectUser(idLeader)).thenReturn(leader);
        when(taskRepository.findById(idTask)).thenReturn(Optional.empty());
        BaseResponse response = taskService.updateTask(idLeader, idTask, taskRequest);
        Assertions.assertEquals(Constant.CODE_ERROR, response.getCode());
        Assertions.assertTrue(response.getMessage().contains("The leader must not be null and the projectid does not match or the email is null"));
        Assertions.assertFalse(response.getData().isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
     void testUpdatePercentage_PercentageOutOfRange() {
        Long idMember = 1L;
        Long taskId = 1L;
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(200);
        ProjectUserResponse member = ProjectUserResponse.builder()
                .fullName("Jane Doe")
                .build();

        Task task = Task.builder()
                .percentageComplete(50)
                .percentageProject(100)
                .build();
        when(projectUserClient.getProjectMemberId(idMember)).thenReturn(member);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        BaseResponse response = taskService.updatePercentage(idMember, taskId, updateTaskRequest, "token");
        Assertions.assertEquals(Constant.CODE_ERROR, response.getCode());
        Assertions.assertTrue(response.getMessage().contains("El porcentaje no puede ser mayor a 100"));
        Assertions.assertFalse(response.getData().isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        Long idLeader = 1L;
        Long idTask = 1L;

        ProjectUserResponse leader = ProjectUserResponse.builder()
                .fullName("John Doe")
                .build();
        when(projectUserClient.getProjectUser(idLeader)).thenReturn(leader);
        when(taskRepository.findById(idTask)).thenReturn(Optional.empty());
        BaseResponse response = taskService.deleteTask(idLeader, idTask);
        Assertions.assertEquals(Constant.CODE_ERROR, response.getCode());
        Assertions.assertTrue(response.getMessage().contains("The leader must not be null and the projectid does not match or the email is null"));
        Assertions.assertFalse(response.getData().isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }


}

