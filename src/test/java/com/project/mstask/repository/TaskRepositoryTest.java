package com.project.mstask.repository;

import com.project.mstask.entity.Task;
import com.project.mstask.repositoy.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSaveTask() {
        Task taskToSave = Task.builder()
                .name("Tarea de prueba")
                .projectID(1)
                .description("Descripción de la tarea de prueba")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .state(1)
                .percentageComplete(0)
                .percentageProject(0)
                .fullNameMember("Miembro de prueba")
                .fullNameLeader("Líder de prueba")
                .createdBy("Usuario de prueba")
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Task savedTask = Task.builder()
                .taskID(1)
                .name("Tarea de prueba")
                .projectID(1)
                .description("Descripción de la tarea de prueba")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .state(1)
                .percentageComplete(0)
                .percentageProject(0)
                .fullNameMember("Miembro de prueba")
                .fullNameLeader("Líder de prueba")
                .createdBy("Usuario de prueba")
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        when(taskRepository.save(taskToSave)).thenReturn(savedTask);

        Task returnedTask = taskRepository.save(taskToSave);

        verify(taskRepository, times(1)).save(taskToSave);

        assertNotNull(returnedTask);
        assertEquals(savedTask.getTaskID(), returnedTask.getTaskID());
        assertEquals(savedTask.getName(), returnedTask.getName());
    }

    @Test
    void testFindById() {
        Long taskId = 1L;
        Task task = Task.builder()
                .taskID(1)
                .name("Tarea de prueba")
                .projectID(1)
                .description("Descripción de la tarea de prueba")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .state(1)
                .percentageComplete(0)
                .percentageProject(0)
                .fullNameMember("Miembro de prueba")
                .fullNameLeader("Líder de prueba")
                .createdBy("Usuario de prueba")
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<Task> retrievedTask = taskRepository.findById(taskId);

        verify(taskRepository, times(1)).findById(taskId);

        assertTrue(retrievedTask.isPresent());
        assertEquals(task.getTaskID(), retrievedTask.get().getTaskID());
        assertEquals(task.getName(), retrievedTask.get().getName());
    }
}