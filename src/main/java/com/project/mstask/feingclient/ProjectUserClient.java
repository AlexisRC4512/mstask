package com.project.mstask.feingclient;

import com.project.mstask.aggregates.response.ProjectResponse;
import com.project.mstask.aggregates.response.ProjectUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "MS-PROJECT")
public interface ProjectUserClient {
    @GetMapping("/api/v1/project/leaderProject/{id}")
    public ProjectUserResponse getProjectUser(@PathVariable Long id) ;
    @GetMapping("/api/v1/project/memberProject")
    public ProjectUserResponse getProjectMember(@RequestParam String email) ;
    @GetMapping("/api/v1/project/memberProject/{id}")
    public ProjectUserResponse getProjectMemberId(@PathVariable Long id);
    @PutMapping("/api/v1/project/update/{id}")
    public ProjectResponse updatePercentAdvance(@PathVariable Long id,@RequestParam int advance,
                                                @RequestParam String authorizationHeader);

}
