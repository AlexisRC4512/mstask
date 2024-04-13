package com.project.mstask.service.impl;

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
import com.project.mstask.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserClient userClient;
    private final ProjectUserClient projectUserClient;
    private final TaskRepository taskRepository;
    @Override
    public boolean validateToken(String token) {
        try {
            if (token.isEmpty())
            {
                return false;
            }
            return userClient.validateApiToken(token);
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return false;
        }
    }

    @Override
    public BaseResponse getTaskForId(Long id) {
        try {

            Optional<Task> taskForId=taskRepository.findById(id);
            if (taskForId.isPresent())
            {
                return new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS, Optional.of(taskForId.get()));
            }
            else {
                return new BaseResponse(Constant.CODE_ERROR_NULL,Constant.MESS_ERROR,Optional.empty());
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return new BaseResponse(Constant.CODE_ERROR,exception.getMessage(),Optional.empty());
        }
    }

    @Override
    public BaseResponse createTask(Long idLeader, Long idProject, Task task, String emailMember) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestampCreate = Timestamp.valueOf(now);
            ProjectUserResponse leaderCreate=projectUserClient.getProjectUser(idLeader);
            ProjectUserResponse memberCreate=projectUserClient.getProjectMember(emailMember);
            if (leaderCreate!=null && leaderCreate.getProject().getProjectId().equals(idProject) || memberCreate!=null){
                Task newTask=Task.builder().createdBy(leaderCreate.getFullName())
                        .dateCreated(timestampCreate)
                        .name(task.getName())
                        .fullNameLeader(leaderCreate.getFullName())
                        .fullNameMember(memberCreate.getFullName())
                        .description(task.getDescription())
                        .percentageComplete(0)
                        .percentageProject(task.getPercentageProject())
                        .leaderID(leaderCreate.getLeaderId().intValue())
                        .memberID(memberCreate.getMemberId().intValue())
                        .state(1)
                        .startDate(task.getStartDate())
                        .endDate(task.getEndDate())
                        .projectID(task.getProjectID())
                        .build();
                taskRepository.save(newTask);
                return new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS,Optional.of(newTask));
            }else {
                return new BaseResponse(Constant.CODE_ERROR_NULL,Constant.MESS_NULL,Optional.empty());
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return new BaseResponse(Constant.CODE_ERROR,exception.getMessage(),Optional.empty());
        }
    }

    @Override
    public BaseResponse updateTask(Long idLeader, Long idTask, TaskRequest task) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            ProjectUserResponse leader=projectUserClient.getProjectUser(idLeader);
            Optional<Task> newTaskUpdate=taskRepository.findById(idTask);
            if (leader!=null && newTaskUpdate.isPresent())
            {
                newTaskUpdate.get().setName(task.getName());
                newTaskUpdate.get().setDescription(task.getDescription());
                newTaskUpdate.get().setDateModified(timestamp);
                newTaskUpdate.get().setFullNameLeader(task.getFullNameLeader());
                newTaskUpdate.get().setFullNameMember(task.getFullNameMember());
                newTaskUpdate.get().setLeaderID(task.getLeaderID());
                newTaskUpdate.get().setMemberID(task.getMemberID());
                newTaskUpdate.get().setModifiedBy(leader.getFullName());
                newTaskUpdate.get().setState(task.getState());
                taskRepository.save(newTaskUpdate.get());
                return new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS,Optional.of(newTaskUpdate.get()));
            }else {
                return new BaseResponse(Constant.CODE_ERROR,"The leader must not be null and the projectid does not match or the email is null",Optional.empty());
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return new BaseResponse(Constant.CODE_ERROR,exception.getMessage(),Optional.empty());
        }
    }

    @Override
    public BaseResponse updatePercentage(Long idMember, Long taskId, UpdateTaskRequest updateTaskRequest, String token) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            ProjectUserResponse member = projectUserClient.getProjectMemberId(idMember);
            Optional<Task> taskOptional = taskRepository.findById(taskId);

            if (member == null || !taskOptional.isPresent()) {
                return new BaseResponse(Constant.CODE_ERROR, "El member o el idTask no son válidos", Optional.empty());
            }

            Task task = taskOptional.get();
            int newPercentage = task.getPercentageComplete() + updateTaskRequest.getPercentageComplete();

            if (newPercentage > 100 || newPercentage < 0) {
                return new BaseResponse(Constant.CODE_ERROR, "El porcentaje no puede ser mayor a 100 ni menor a 0", Optional.empty());
            }

            task.setPercentageComplete(newPercentage);
            if (newPercentage > 100) {
                task.setPercentageComplete(100);
                int percentageProject = task.getPercentageProject() == 100 ? 100 : task.getPercentageProject();
                getInfoProject(member.getProject().getProjectId(), percentageProject, token);
            }

            task.setModifiedBy(member.getFullName());
            task.setDateModified(timestamp);
            taskRepository.save(task);

            return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(task));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new BaseResponse(Constant.CODE_ERROR, exception.getMessage(), Optional.empty());
        }
    }

    @Override
    public BaseResponse deleteTask(Long idLeader, Long idTask) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            ProjectUserResponse leader=projectUserClient.getProjectUser(idLeader);
            Optional<Task> task =taskRepository.findById(idTask);
            if (leader!=null && task.isPresent())
            {
                task.get().setDeletedBy(leader.getFullName());
                task.get().setDateDeleted(timestamp);
                task.get().setState(0);
                taskRepository.save(task.get());
                return new BaseResponse(Constant.CODE_SUCCESS,Constant.MESS_SUCCESS,Optional.of(task.get()));
            }else {
                return new BaseResponse(Constant.CODE_ERROR,"The leader must not be null and the projectid does not match or the email is null\n",Optional.empty());
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return new BaseResponse(Constant.CODE_ERROR,exception.getMessage(),Optional.empty());
        }
    }
    private ProjectResponse getInfoProject(Long id,int advance,String token){
        try {
            ProjectResponse project=projectUserClient.updatePercentAdvance(id, advance, token);
            if (project!=null)
            {
                return project;
            }else {
                return null;
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return null;
        }
    }
}
