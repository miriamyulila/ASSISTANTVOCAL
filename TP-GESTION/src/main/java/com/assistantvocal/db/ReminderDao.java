package com.assistantvocal.db;

import com.assistantvocal.model.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReminderDao {
    private final Database database;

    public ReminderDao(Database database) {
        this.database = database;
    }

    public void save(Reminder reminder) {
        String sql = "INSERT INTO reminders(title, description, due_date) VALUES(?,?,?)";
        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reminder.getTitle());
            stmt.setString(2, reminder.getDescription());
            stmt.setString(3, reminder.getDueDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not save reminder: " + e.getMessage());
        }
    }

    public List<Reminder> findAll() {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT id, title, description, due_date FROM reminders ORDER BY id";
        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reminders.add(new Reminder(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Could not read reminders: " + e.getMessage());
        }
        return reminders;
    }
}
