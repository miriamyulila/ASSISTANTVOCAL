package com.assistantvocal.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String url;

    public Database(String dbFileName) {
        this.url = "jdbc:sqlite:" + dbFileName;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void initialize() {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS reminders (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT, due_date TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS work_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL, duration_minutes INTEGER NOT NULL, session_date TEXT NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}
