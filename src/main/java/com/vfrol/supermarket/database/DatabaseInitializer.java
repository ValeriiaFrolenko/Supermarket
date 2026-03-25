package com.vfrol.supermarket.database;

import com.google.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    private final DatabaseConnection databaseConnection;

    @Inject
    public DatabaseInitializer(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void initialize() {
        executeSqlScript();
    }

    private void executeSqlScript() {
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            var inputStream = getClass().getResourceAsStream("/sql/schema.sql");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + "/sql/schema.sql");
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
