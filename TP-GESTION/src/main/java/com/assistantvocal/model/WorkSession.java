package com.assistantvocal.model;

public class WorkSession {
    private final int id;
    private final String description;
    private final int durationMinutes;
    private final String sessionDate;

    public WorkSession(int id, String description, int durationMinutes, String sessionDate) {
        this.id = id;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.sessionDate = sessionDate;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %d min on %s", id, description, durationMinutes, sessionDate);
    }
}
