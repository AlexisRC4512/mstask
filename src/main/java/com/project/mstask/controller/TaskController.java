package com.project.mstask.controller;

import com.project.mstask.aggregates.constants.Constant;
import com.project.mstask.aggregates.request.TaskRequest;
import com.project.mstask.aggregates.request.UpdateTaskRequest;
import com.project.mstask.aggregates.response.BaseResponse;
import com.project.mstask.entity.Task;
import com.project.mstask.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping("/{taskId}")
    public BaseResponse getTaskById(@PathVariable Long taskId,@RequestParam String authorizationHeader) {
        if (taskService.validateToken(authorizationHeader))
        {
            return taskService.getTaskForId(taskId);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTHORIZED, Constant.NOT_AUTHORIZED, Optional.empty());
        }
    }
    @PostMapping("/create")
    public BaseResponse createTask(@RequestParam Long idLeader,
                                   @RequestParam Long idProject,
                                   @RequestBody Task task,
                                   @RequestParam String emailMember ,
                                   @RequestParam String authorizationHeader) {
        if (taskService.validateToken(authorizationHeader))
        {
            return taskService.createTask(idLeader,idProject,task,emailMember);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTHORIZED,Constant.NOT_AUTHORIZED, Optional.empty());
        }
    }
    @PutMapping("/update/{taskId}")
    public BaseResponse updateTask(@RequestParam Long idLeader,
                                   @PathVariable Long taskId,
                                   @RequestBody TaskRequest task ,@RequestParam String authorizationHeader) {
        if (taskService.validateToken(authorizationHeader))
        {
            return taskService.updateTask(idLeader,taskId,task);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTHORIZED,Constant.NOT_AUTHORIZED, Optional.empty());
        }
    }
    @PutMapping("/updatePercentage/{idMember}")
    public BaseResponse updateTaskPercentage(@PathVariable Long idMember,
                                              @RequestParam Long taskId,
                                             @RequestBody UpdateTaskRequest updateTaskRequest,
                                             @RequestParam String authorizationHeader) {
        if (taskService.validateToken(authorizationHeader))
        {
            return taskService.updatePercentage(idMember,taskId,updateTaskRequest,authorizationHeader);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTHORIZED,Constant.NOT_AUTHORIZED, Optional.empty());
        }
    }
    @DeleteMapping("/delete/{taskId}")
    public BaseResponse deleteTask(@RequestParam Long idLeader,
                                   @PathVariable Long taskId
                                    ,@RequestParam String authorizationHeader) {
        if (taskService.validateToken(authorizationHeader))
        {
            return taskService.deleteTask(idLeader,taskId);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTHORIZED,Constant.NOT_AUTHORIZED, Optional.empty());
        }
    }

}
