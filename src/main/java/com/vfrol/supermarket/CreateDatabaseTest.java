package com.vfrol.supermarket;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vfrol.supermarket.database.DatabaseInitializer;
import org.jdbi.v3.core.Jdbi;

import java.nio.charset.StandardCharsets;

// This class is used only for testing purposes to create the database schema and seed it with initial data. It should not be used in production.
public class CreateDatabaseTest {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SupermarketModule());
        DatabaseInitializer initializer = injector.getInstance(DatabaseInitializer.class);
        initializer.initialize();
        System.out.println("Schema created.");
        Jdbi jdbi = injector.getInstance(Jdbi.class);
        seedDatabase(jdbi);
        System.out.println("Database seeded successfully!");
    }

    private static void seedDatabase(Jdbi jdbi) {
        try {
            var inputStream = CreateDatabaseTest.class.getResourceAsStream("/sql/seed.sql");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /sql/seed.sql");
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            jdbi.useHandle(handle -> handle.createScript(sql).execute());
        } catch (Exception e) {
            System.err.println("Database seeding failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}