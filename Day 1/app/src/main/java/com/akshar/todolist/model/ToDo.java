package com.akshar.todolist.model;

public class ToDo {
    private String title;
    private String description;
    private String date;
    private String time;
    private String id;
    private boolean isCompleted;

    public ToDo() {
    }

    public ToDo(String title, String description, String date, String time, String id, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.id = id;
        this.isCompleted = isCompleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
