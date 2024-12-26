package com.example.planit.entity.note;

public class Note {
    private int id;
    private String title;
    private String content;

    // Constructor, getters y setters
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}