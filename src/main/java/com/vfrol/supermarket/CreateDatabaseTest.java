package com.vfrol.supermarket;

import com.vfrol.supermarket.database.DatabaseConnection;
import com.vfrol.supermarket.database.DatabaseInitializer;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

// run this class to create the database and seed it with initial data
public class CreateDatabaseTest {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseInitializer initializer = new DatabaseInitializer(databaseConnection);
        initializer.initialize();
            try (Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement()) {
                var inputStream = CreateDatabaseTest.class.getResourceAsStream("/sql/seed.sql");
                if (inputStream == null) {
                    throw new RuntimeException("Resource not found: " + "/sql/seed.sql");
                }
                String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                statement.executeUpdate(sql);
            } catch (Exception e) {
                System.err.println("Database initialization failed: " + e.getMessage());
                throw new RuntimeException(e);
            }
    }
}
