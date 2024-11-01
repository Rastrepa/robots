package com.example.robots.controller;

import com.example.robots.model.LogEntry;
import com.example.robots.servise.ActivityTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ActivityTrackerService activityTrackerService;

    @GetMapping("/")
    public String index(Model model) {
        List<LogEntry> logs = activityTrackerService.getLogs();
        model.addAttribute("logs", logs);
        return "index";
    }
}