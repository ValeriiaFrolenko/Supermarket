package com.vfrol.supermarket.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.*;
import com.vfrol.supermarket.database.H2ExceptionTranslator;
import com.vfrol.supermarket.database.JdbiFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

public class SupermarketModule extends AbstractModule {

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./supermarket;MODE=MySQL;DATABASE_TO_UPPER=FALSE;IGNORECASE=TRUE;");
        return dataSource;
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource, H2ExceptionTranslator exceptionTranslator) {
        return new JdbiFactory(exceptionTranslator)
                .create(dataSource);
    }

    @Provides
    @Singleton
    public EmployeeDAO provideEmployeeDAO(Jdbi jdbi) { return jdbi.onDemand(EmployeeDAO.class); }

    @Provides
    @Singleton
    public CategoryDAO provideCategoryDAO(Jdbi jdbi) { return jdbi.onDemand(CategoryDAO.class); }

    @Provides
    @Singleton
    public ProductDAO provideProductDAO(Jdbi jdbi) { return jdbi.onDemand(ProductDAO.class); }

    @Provides
    @Singleton
    public CustomerCardDAO provideCustomerCardDAO(Jdbi jdbi) { return jdbi.onDemand(CustomerCardDAO.class); }

    @Provides
    @Singleton
    public StoreProductDAO provideStoreProductDAO(Jdbi jdbi) { return jdbi.onDemand(StoreProductDAO.class); }

    @Provides
    @Singleton
    public SaleDAO provideSaleDAO(Jdbi jdbi) { return jdbi.onDemand(SaleDAO.class); }

    @Provides
    @Singleton
    public CheckDAO provideCheckDAO(Jdbi jdbi) { return jdbi.onDemand(CheckDAO.class); }

    @Provides @Singleton
    public SalesAnalyticsDAO provideSalesAnalyticsDAO(Jdbi jdbi) {
        return jdbi.onDemand(SalesAnalyticsDAO.class);
    }

    @Provides @Singleton
    public EmployeePerformanceDAO provideEmployeePerformanceDAO(Jdbi jdbi) {
        return jdbi.onDemand(EmployeePerformanceDAO.class);
    }

}