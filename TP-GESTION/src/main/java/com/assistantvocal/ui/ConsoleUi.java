package com.assistantvocal.ui;

import com.assistantvocal.model.Reminder;
import com.assistantvocal.model.WorkSession;
import com.assistantvocal.service.LauncherService;
import com.assistantvocal.service.ReminderService;
import com.assistantvocal.service.WorkTimeTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleUi {
    private final ReminderService reminderService;
    private final WorkTimeTracker workTimeTracker;
    private final LauncherService launcherService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ConsoleUi(ReminderService reminderService, WorkTimeTracker workTimeTracker, LauncherService launcherService) {
        this.reminderService = reminderService;
        this.workTimeTracker = workTimeTracker;
        this.launcherService = launcherService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Assistant Vocal - Gestion Intelligente ===");

        while (true) {
            System.out.println();
            System.out.println("1. Recherche web");
            System.out.println("2. Ouvrir une application");
            System.out.println("3. Ajouter un rappel");
            System.out.println("4. Voir le calendrier / rappels");
            System.out.println("5. Enregistrer une session de travail");
            System.out.println("6. Voir le temps de travail total");
            System.out.println("7. Quitter");
            System.out.print("Choix: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> searchWeb();
                case "2" -> openApplication();
                case "3" -> addReminder();
                case "4" -> showReminders();
                case "5" -> logWorkSession();
                case "6" -> showWorkSummary();
                case "7" -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide. Réessayez.");
            }
        }
    }

    private void searchWeb() {
        System.out.print("Entrez la recherche: ");
        String query = scanner.nextLine();
        launcherService.searchWeb(query);
    }

    private void openApplication() {
        System.out.print("Entrez la commande de l'application (ex: notepad.exe): ");
        String command = scanner.nextLine();
        launcherService.openApp(command);
    }

    private void addReminder() {
        System.out.print("Titre: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Date d'échéance (yyyy-MM-dd) [laisser vide pour aujourd'hui]: ");
        String dueDate = scanner.nextLine();
        if (dueDate.isBlank()) {
            dueDate = LocalDate.now().format(dateFormatter);
        }
        reminderService.createReminder(title, description, dueDate);
        System.out.println("Rappel enregistré.");
    }

    private void showReminders() {
        List<Reminder> reminders = reminderService.getReminders();
        if (reminders.isEmpty()) {
            System.out.println("Aucun rappel trouvé.");
            return;
        }
        System.out.println("=== Rappels et agenda ===");
        reminders.forEach(System.out::println);
    }

    private void logWorkSession() {
        System.out.print("Description de la tâche: ");
        String description = scanner.nextLine();
        System.out.print("Durée en minutes: ");
        int duration = parseInt(scanner.nextLine());
        String sessionDate = LocalDate.now().format(dateFormatter);
        workTimeTracker.logSession(description, duration, sessionDate);
        System.out.println("Session de travail enregistrée.");
    }

    private void showWorkSummary() {
        List<WorkSession> sessions = workTimeTracker.getSessions();
        if (sessions.isEmpty()) {
            System.out.println("Aucune session de travail enregistrée.");
            return;
        }
        System.out.println("=== Sessions de travail ===");
        sessions.forEach(System.out::println);
        int total = workTimeTracker.getTotalMinutes();
        System.out.printf("Temps total travaillé: %d minutes (%.2f heures)%n", total, total / 60.0);
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Valeur invalide, utilisation de 0.");
            return 0;
        }
    }
}
