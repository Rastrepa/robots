package com.example.robots.impl;

import com.example.robots.Robot;
import com.example.robots.Task;
import com.example.robots.servise.ActivityTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasicRobot implements Robot {
    private String robotId;
    private volatile boolean alive = true;
    private final ActivityTrackerService activityTrackerService;

    // Используем @Value для инжекции robotId и ActivityTrackerService
    @Autowired
    public BasicRobot(@Value("${robot.id}") String robotId, ActivityTrackerService activityTrackerService) {
        this.robotId = robotId;
        this.activityTrackerService = activityTrackerService;
    }

    @Override
    public void executeTask(Task task) {
        if (!isAlive()) {
            throw new IllegalStateException("Robot is dead!");
        }

        System.out.println("Robot " + robotId + " executing task: " + task.getTaskName());
        //System.out.println("Task received: " + task.getTaskName().toLowerCase());
        String taskName = task.getTaskName().replace("\"", "").trim().toLowerCase();
        System.out.println("Task received: " + taskName);

        switch (taskName) {
            case "die":
                die();
                return;
            case "move":
                // Логика для перемещения робота
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "clean":
                // Логика для уборки
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("Unknown task: " + taskName);
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void die() {
        alive = false;
        String message = "Robot " + robotId + " killed itself.";

        System.out.println(message);
        activityTrackerService.addToLog(message);
    }

    @Override
    public String getRobotId() {
        return robotId;
    }
}
