package com.example.robots.model;

import lombok.Data;

@Data
public class QueueTask {
    private String robotId;
    private String taskName;

    public QueueTask(String robotId, String taskName){
        this.robotId = robotId;
        this.taskName = taskName;
    }
}