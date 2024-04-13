package com.project.mstask.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Task_ID")
    private int taskID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Project_ID", nullable = false)
    private int projectID;

    @Column(name = "Description")
    private String description;

    @Column(name = "Star_Date", nullable = false)
    private Timestamp startDate;

    @Column(name = "End_Date", nullable = false)
    private Timestamp endDate;

    @Column(name = "State", nullable = false)
    private int state;

    @Column(name = "Percentage_complete", nullable = false)
    private int percentageComplete;
    @Column(name = "Percentage_project", nullable = false)
    private int percentageProject;

    @Column(name = "Member_ID")
    private Integer memberID;

    @Column(name = "leader_id")
    private Integer leaderID;

    @Column(name = "full_name_Member", nullable = false, length = 200)
    private String fullNameMember;
    @Column(name = "full_name_Leader", nullable = false, length = 200)
    private String fullNameLeader;
    @Column(name = "Created_by", length = 45)
    private String createdBy;

    @Column(name = "Date_created")
    private Timestamp dateCreated;

    @Column(name = "Modified_by", length = 45)
    private String modifiedBy;

    @Column(name = "Date_modified")
    private Timestamp dateModified;

    @Column(name = "Deleted_by", length = 45)
    private String deletedBy;

    @Column(name = "Date_deleted")
    private Timestamp dateDeleted;


}