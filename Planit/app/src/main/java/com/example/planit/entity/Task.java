package com.example.planit.entity;

public class Task {
    private String id;
    private String name;
    private String description;
    private boolean completed;
    private int importance;

    public Task(String id, String name, String description, boolean completed, int importance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = completed;
        this.importance = importance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getImportance() {
        return importance;
    }
}
