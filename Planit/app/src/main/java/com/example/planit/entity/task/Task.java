package com.example.planit.entity.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Task {
    private int id;
    private String name;
    private String description;
    private boolean completed;
    private int importance;
    private String type;
    private int subjectId;
    private String dueDate;
    private String dueTime;

    public Task(int id, String name, String description, boolean completed, int importance, String type, int subjectId, String dueDate, String dueTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = completed;
        this.importance = importance;
        this.type = type;
        this.subjectId = subjectId;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getCompleted() {
        return completed;
    }

    public int getImportance() {
        return importance;
    }

    public String getType() {
        return type;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formatDateReversed(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
