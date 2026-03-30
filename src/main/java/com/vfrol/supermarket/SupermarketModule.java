package com.vfrol.supermarket;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.EmployeeDAO;
import com.vfrol.supermarket.database.DatabaseInitializer;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;

public class SupermarketModule extends AbstractModule {

    @Provides
    @Singleton
    public DatabaseInitializer provideDatabaseInitializer(Jdbi jdbi) {
        return new DatabaseInitializer(jdbi);
    }

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./supermarket;");
        return dataSource;
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        return Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
    }

    @Provides
    public EmployeeDAO provideEmployeeDAO(Jdbi jdbi) {
        return jdbi.onDemand(EmployeeDAO.class);
    }
}