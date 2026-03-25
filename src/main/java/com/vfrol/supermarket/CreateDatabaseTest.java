package com.vfrol.supermarket;

import com.vfrol.supermarket.database.DatabaseConnection;
import com.vfrol.supermarket.database.DatabaseInitializer;

public class CreateDatabaseTest {
    public static void main(String[] args) {
        DatabaseInitializer initializer = new DatabaseInitializer(new DatabaseConnection());
        initializer.initialize();
        initializer.executeSqlScript("/sql/seed.sql");
    }
}
