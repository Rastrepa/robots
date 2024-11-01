package com.example.robots.controller;

import com.example.robots.Robot;
import com.example.robots.impl.BasicRobot;
import com.example.robots.model.*;
import com.example.robots.servise.ActivityTrackerService;
import com.example.robots.servise.TaskExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RobotController {

    private final TaskExecutorService taskExecutorService;
    private final ActivityTrackerService activityTrackerService;

    @PostMapping("/task/{robotId}")
    public ResponseMessage assignTask(@PathVariable String robotId, @RequestBody String taskName) {
        if ("all".equalsIgnoreCase(robotId)) {
            // Если robotId равен "all", отправляем задание всем роботам
            return taskExecutorService.sendBroadcastTask(taskName);
        } else {
            // Иначе добавляем задание для конкретного робота
            taskExecutorService.enqueueTask(new QueueTask(robotId, taskName));
            return new SuccessResponse();
        }
    }

    @GetMapping("/logs")
    public List<LogEntry> getLogs() {
        return activityTrackerService.getLogs();
    }

    @GetMapping("/robots")
    public List<String> getRobots() {
        return activityTrackerService.getAllRobots().stream()
                .map(Robot::getRobotId)
                .collect(Collectors.toList());
    }

    @PostMapping("/robots/register")
    public ResponseMessage registerRobot(@RequestBody NewRobotRequest newRobotRequest) {
        // Измените создание нового робота, передав activityTrackerService
        BasicRobot newRobot = new BasicRobot(newRobotRequest.getRobotId(), activityTrackerService);
        activityTrackerService.registerRobot(newRobot);
        return new SuccessResponse();
    }

    @GetMapping("/pending-tasks")
    public List<QueueTask> getPendingTasks() {
        return taskExecutorService.getPendingTasks();
    }
}
