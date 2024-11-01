package com.example.robots.servise;

import com.example.robots.impl.BasicRobot;
import com.example.robots.model.LogEntry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ActivityTrackerService {
    private final Logger logger = LoggerFactory.getLogger(ActivityTrackerService.class);

    private final Map<String, BasicRobot> robots = new ConcurrentHashMap<>();
    private final List<LogEntry> logEntries = new ArrayList<>();

    /* Регистрация робота в системе */
    public synchronized void registerRobot(BasicRobot robot) {
        robots.putIfAbsent(robot.getRobotId(), robot);
        logger.info("Registered robot with ID: {}", robot.getRobotId());
        //addToLog("New robot registered: " + robot.getRobotId());
    }

    // Проверяем состояние роботов каждые 10 секунд
    @Scheduled(fixedRate = 10000)
    public synchronized void checkRobots() {
        for (var entry : robots.entrySet()) {
            var robot = entry.getValue();
            if (!robot.isAlive()) {
                robots.remove(entry.getKey());
                logger.warn("Robot {} is dead.", entry.getKey());
                addToLog("Robot died: " + entry.getKey());
            }
        }

        if (robots.isEmpty()) {
            createNewRobot();
        }
    }

    /**
     * Создание нового робота
     */
    private static int robotCounter = 1; // счётчик для "уникального" идентификатора
    private void createNewRobot() {
        String newRobotId = "R-" + robotCounter++;
        var newRobot = new BasicRobot(newRobotId, this);
        registerRobot(newRobot);
        addToLog("Created new robot: " + newRobot.getRobotId());
    }

    public List<BasicRobot> getAllRobots() {
        return new ArrayList<>(robots.values());
    }

    /** Добавление записи в лог */
    public synchronized void addToLog(String message) {
        logEntries.add(new LogEntry(message, System.currentTimeMillis()));
    }

    public synchronized List<LogEntry> getLogs() {
        return logEntries;
    }
}
