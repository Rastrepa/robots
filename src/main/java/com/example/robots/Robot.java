package com.example.robots;

public interface Robot {
    void executeTask(Task task);
    String getRobotId();
    boolean isAlive();
    void die();
}