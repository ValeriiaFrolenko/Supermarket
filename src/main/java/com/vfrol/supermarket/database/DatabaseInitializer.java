package com.vfrol.supermarket.database;

import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.nio.charset.StandardCharsets;

public class DatabaseInitializer {
    private final Jdbi jdbi;

    @Inject
    public DatabaseInitializer(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void initialize() {
        try {
            var inputStream = getClass().getResourceAsStream("/sql/schema.sql");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /sql/schema.sql");
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            jdbi.useHandle(handle -> handle.createScript(sql).execute());
        } catch (Exception e) {
            throw new RuntimeException("Database schema initialization failed", e);
        }
    }
}