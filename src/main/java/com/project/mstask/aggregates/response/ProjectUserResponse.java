package com.project.mstask.aggregates.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ProjectUserResponse {
    private Long projectUserId;
    private String roleUser;
    private Integer state;
    private Long leaderId;
    private Long memberId;
    private ProjectResponse project;
    private String email;
    private String fullName;
    private String createdBy;
    private Timestamp dateCreated;
    private String modifiedBy;
    private Timestamp dateModified;
    private String deletedBy;
    private Timestamp dateDeleted;
}
