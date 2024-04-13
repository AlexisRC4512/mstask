package com.project.mstask.aggregates.request;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
    private int percentageComplete;
}
