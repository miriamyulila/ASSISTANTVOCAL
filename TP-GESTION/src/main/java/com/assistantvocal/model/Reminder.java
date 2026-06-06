package com.assistantvocal.model;

public class Reminder {
    private final int id;
    private final String title;
    private final String description;
    private final String dueDate;

    public Reminder(int id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (Due: %s)", id, title, description, dueDate);
    }
}
