package com.vfrol.supermarket.database;

import com.google.inject.Inject;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DataSource dataSource;

    @Inject
    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialize() {
        executeSqlScript();
    }

    private void executeSqlScript() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var inputStream = getClass().getResourceAsStream("/sql/schema.sql");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /sql/schema.sql");
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}