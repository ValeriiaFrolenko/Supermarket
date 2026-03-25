package com.vfrol.supermarket;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.vfrol.supermarket.database.DatabaseConnection;
import com.vfrol.supermarket.database.DatabaseInitializer;

public class SupermarketModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DatabaseConnection.class).in(Scopes.SINGLETON);
        bind(DatabaseInitializer.class).in(Scopes.SINGLETON);
    }
}
