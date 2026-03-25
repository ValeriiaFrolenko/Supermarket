package com.vfrol.supermarket.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:supermarket.db";

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        try (var stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}
