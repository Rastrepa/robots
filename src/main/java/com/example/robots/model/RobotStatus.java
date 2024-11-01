package com.example.robots.model;

import lombok.Data;

@Data
public class RobotStatus {
    private boolean isAlive = true;
    private int taskCount = 0;
}