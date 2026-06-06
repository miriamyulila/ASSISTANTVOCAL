package com.assistantvocal.db;

import com.assistantvocal.model.WorkSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkSessionDao {
    private final Database database;

    public WorkSessionDao(Database database) {
        this.database = database;
    }

    public void save(WorkSession session) {
        String sql = "INSERT INTO work_sessions(description, duration_minutes, session_date) VALUES(?,?,?)";
        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, session.getDescription());
            stmt.setInt(2, session.getDurationMinutes());
            stmt.setString(3, session.getSessionDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not save work session: " + e.getMessage());
        }
    }

    public List<WorkSession> findAll() {
        List<WorkSession> sessions = new ArrayList<>();
        String sql = "SELECT id, description, duration_minutes, session_date FROM work_sessions ORDER BY id";
        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sessions.add(new WorkSession(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getInt("duration_minutes"),
                        rs.getString("session_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Could not read work sessions: " + e.getMessage());
        }
        return sessions;
    }
}
