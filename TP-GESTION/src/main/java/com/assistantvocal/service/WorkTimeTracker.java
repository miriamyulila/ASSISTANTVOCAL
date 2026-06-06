package com.assistantvocal.service;

import com.assistantvocal.db.Database;
import com.assistantvocal.db.WorkSessionDao;
import com.assistantvocal.model.WorkSession;

import java.util.List;

public class WorkTimeTracker {
    private final WorkSessionDao workSessionDao;

    public WorkTimeTracker(Database database) {
        this.workSessionDao = new WorkSessionDao(database);
    }

    public void logSession(String description, int durationMinutes, String sessionDate) {
        WorkSession session = new WorkSession(0, description, durationMinutes, sessionDate);
        workSessionDao.save(session);
    }

    public List<WorkSession> getSessions() {
        return workSessionDao.findAll();
    }

    public int getTotalMinutes() {
        return getSessions().stream().mapToInt(WorkSession::getDurationMinutes).sum();
    }
}
