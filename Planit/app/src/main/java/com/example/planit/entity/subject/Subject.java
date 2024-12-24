package com.example.planit.entity.subject;

public class Subject {
    private int id;
    private String name;
    private int color;

    public Subject(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}