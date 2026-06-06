package com.assistantvocal.service;

import com.assistantvocal.db.Database;
import com.assistantvocal.db.ReminderDao;
import com.assistantvocal.model.Reminder;

import java.util.List;

public class ReminderService {
    private final ReminderDao reminderDao;

    public ReminderService(Database database) {
        this.reminderDao = new ReminderDao(database);
    }

    public void createReminder(String title, String description, String dueDate) {
        Reminder reminder = new Reminder(0, title, description, dueDate);
        reminderDao.save(reminder);
    }

    public List<Reminder> getReminders() {
        return reminderDao.findAll();
    }
}
