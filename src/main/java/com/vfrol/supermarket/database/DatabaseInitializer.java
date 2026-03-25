package com.vfrol.supermarket.database;

import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    private final DatabaseConnection databaseConnection;

    @Inject
    public DatabaseInitializer(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void initialize() {
        executeSqlScript("/sql/schema.sql");
    }

    public void executeSqlScript(String resourcePath) {
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            var is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Resource not found: " + resourcePath);
                return;
            }
            String sql = new String(is.readAllBytes());
            String[] commands = sql.split(";");
            for (String command : commands) {
                String trimmed = command.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    statement.addBatch(trimmed);
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            System.err.println("Failed to execute SQL script: " + e.getMessage());
            throw new RuntimeException("SQL script execution failed", e);
        }
    }
}
