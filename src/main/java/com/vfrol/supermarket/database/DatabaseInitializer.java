package com.vfrol.supermarket.database;

import com.google.inject.Inject;

public class DatabaseInitializer {

    private final DatabaseConnection databaseConnection;

    @Inject
    public DatabaseInitializer(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public static void initialize() {
        //TODO: Implement database initialization logic, such as creating tables and inserting initial data
    }
}
