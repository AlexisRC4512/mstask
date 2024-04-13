package com.project.mstask.aggregates.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long projectId;
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp deliveryDate;
    private Integer state;
    private Integer percentageComplete;
    private String projectCreatedBy;
    private Timestamp dateCreated;
    private String projectModifiedBy;
    private Timestamp dateModified;
    private String projectDeletedBy;
    private Timestamp dateDeleted;
}
