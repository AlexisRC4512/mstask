package com.project.mstask.aggregates.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String name;
    private int projectID;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private int state;
    private int percentageComplete;
    private Integer memberID;
    private Integer leaderID;
    private String fullNameMember;
    private String fullNameLeader;
}
