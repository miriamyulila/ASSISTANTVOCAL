package com.assistantvocal;

import com.assistantvocal.db.Database;
import com.assistantvocal.service.LauncherService;
import com.assistantvocal.service.ReminderService;
import com.assistantvocal.service.WorkTimeTracker;
import com.assistantvocal.ui.ConsoleUi;

public class AssistantApp {
    public static void main(String[] args) {
        Database database = new Database("assistant.db");
        database.initialize();

        ReminderService reminderService = new ReminderService(database);
        WorkTimeTracker workTimeTracker = new WorkTimeTracker(database);
        LauncherService launcherService = new LauncherService();

        ConsoleUi ui = new ConsoleUi(reminderService, workTimeTracker, launcherService);
        ui.run();
    }
}
