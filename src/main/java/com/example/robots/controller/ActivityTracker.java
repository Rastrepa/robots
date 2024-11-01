package com.example.robots.controller;

import com.example.robots.Task;
import com.example.robots.impl.BasicRobot;
import com.example.robots.model.LogEntry;
import com.example.robots.servise.ActivityTrackerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trackers")
@RequiredArgsConstructor
public class ActivityTracker {
    private static final Logger logger = LoggerFactory.getLogger(ActivityTracker.class);

    private final ActivityTrackerService activityTrackerService;


    @PostMapping("/register")
    public void registerRobot(@RequestBody BasicRobot robot) {
        activityTrackerService.registerRobot(robot);
        logger.info("Registered robot: {}", robot.getRobotId());
    }

    @GetMapping("/logs")
    public List<LogEntry> getLogs() {
        return activityTrackerService.getLogs();
    }

    // Метод для проверки состояния роботов каждые 10 секунд
    @Scheduled(fixedRate = 10000)
    public void checkRobots() {
        activityTrackerService.checkRobots();
    }
}