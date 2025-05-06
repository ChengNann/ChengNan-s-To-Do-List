package com.example.to_do_list;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String description;
    private String time;
    private String category;
    private int priority; // 可选字段，默认值可为0

    public Task(String title, String description, String time, String category) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.category = category;
        this.priority = 0;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTime() { return time; }
    public String getCategory() { return category; }
    public int getPriority() { return priority; }

    // Setters
    public void setPriority(int priority) { this.priority = priority; }
}
