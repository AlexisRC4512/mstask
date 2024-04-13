package com.project.mstask.service;


import com.project.mstask.aggregates.request.TaskRequest;
import com.project.mstask.aggregates.request.UpdateTaskRequest;
import com.project.mstask.aggregates.response.BaseResponse;
import com.project.mstask.entity.Task;


public interface TaskService {
    boolean validateToken(String token);
    BaseResponse getTaskForId(Long id);
    BaseResponse createTask(Long idLeader, Long idProject, Task task,String emailMember);
    BaseResponse updateTask(Long idLeader, Long idTask, TaskRequest task);
    BaseResponse updatePercentage(Long idMember, Long task, UpdateTaskRequest updateTaskRequest, String token);
    BaseResponse deleteTask(Long idLeader,Long idTask);
}
