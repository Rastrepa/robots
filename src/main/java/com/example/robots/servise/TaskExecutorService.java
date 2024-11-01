package com.example.robots.servise;



import com.example.robots.Task;
import com.example.robots.impl.BasicRobot;
import com.example.robots.model.QueueTask;
import com.example.robots.model.ResponseMessage;
import com.example.robots.model.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskExecutorService {
    private final ActivityTrackerService activityTrackerService;

    private final Queue<QueueTask> tasks = new LinkedList<>();

    public void enqueueTask(QueueTask queueTask) {
        tasks.offer(queueTask);
        activityTrackerService.addToLog("Enqueued task: " + queueTask.getTaskName() + " for robot " + queueTask.getRobotId());
    }

    @Scheduled(fixedRate = 500) // Вызывается каждые 500 мс
    public void processTasks() {
        while (!tasks.isEmpty()) {
            var task = tasks.poll();
            Optional<BasicRobot> robotOptional = findRobotById(task.getRobotId());
            if (robotOptional.isPresent()) {
                var robot = robotOptional.get();
                try {
                    robot.executeTask(new Task(task.getTaskName()));
                    activityTrackerService.addToLog("Executed task: " + task.getTaskName() + " by robot " + task.getRobotId());
                } catch (IllegalStateException e) {
                    activityTrackerService.addToLog("Failed to execute task: " + task.getTaskName() + " by robot " + task.getRobotId() + ". Reason: " + e.getMessage());
                }
            } else {
                activityTrackerService.addToLog("No such robot found: " + task.getRobotId());
            }
        }
    }

    private Optional<BasicRobot> findRobotById(String id) {
        return activityTrackerService.getAllRobots().stream()
                .filter(r -> r.getRobotId().equals(id))
                .findFirst();
    }

    public List<QueueTask> getPendingTasks() {
        return new ArrayList<>(tasks);
    }

    public ResponseMessage sendBroadcastTask(String taskName) {
        List<BasicRobot> allRobots = activityTrackerService.getAllRobots();
        for (BasicRobot robot : allRobots) {
            enqueueTask(new QueueTask(robot.getRobotId(), taskName));
        }
        return new SuccessResponse();
    }


}