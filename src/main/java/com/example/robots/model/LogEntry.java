package com.example.robots.model;

import lombok.Data;

@Data
public class LogEntry {
    private String message;
    private long timestamp;

    public LogEntry(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}