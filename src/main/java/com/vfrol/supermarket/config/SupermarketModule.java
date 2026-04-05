package com.vfrol.supermarket.config;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.*;
import com.vfrol.supermarket.database.DatabaseInitializer;
import com.vfrol.supermarket.service.*;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
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
        dataSource.setUrl("jdbc:h2:./supermarket;MODE=MySQL;DATABASE_TO_UPPER=FALSE;IGNORECASE=TRUE");
        return dataSource;
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        return jdbi;
    }

    @Provides
    @Singleton
    public ViewManager provideViewManager(Injector injector) {
        return new ViewManager(injector);
    }

    @Provides
    @Singleton
    public SessionManager provideSessionManager() {
        return new SessionManager();
    }

    @Provides
    @Singleton
    public EmployeeDAO provideEmployeeDAO(Jdbi jdbi) {
        return jdbi.onDemand(EmployeeDAO.class);
    }

    @Provides
    @Singleton
    public CategoryDAO provideCategoryDAO(Jdbi jdbi) {
        return jdbi.onDemand(CategoryDAO.class);
    }

    @Provides
    @Singleton
    public ProductDAO provideProductDAO(Jdbi jdbi) {
        return jdbi.onDemand(ProductDAO.class);
    }

    @Provides
    @Singleton
    public CustomerCardDAO provideCustomerCardDAO(Jdbi jdbi) {
        return jdbi.onDemand(CustomerCardDAO.class);
    }

    @Provides
    @Singleton
    public StoreProductDAO provideStoreProductDAO(Jdbi jdbi){
        return jdbi.onDemand(StoreProductDAO.class);
    }

    @Provides
    @Singleton
    public EmployeeService provideEmployeeService(EmployeeDAO employeeDAO) {
        return new EmployeeService(employeeDAO);
    }

    @Provides
    @Singleton
    public CategoryService provideCategoryService(CategoryDAO categoryDAO) {
        return new CategoryService(categoryDAO);
    }

    @Provides
    @Singleton
    public ProductService provideProductService(ProductDAO productDAO) {
        return new ProductService(productDAO);
    }

    @Provides
    @Singleton
    public CustomerCardService provideCustomerCardService(CustomerCardDAO customerCardDAO) {
        return new CustomerCardService(customerCardDAO);
    }

    @Provides
    @Singleton
    public StoreProductService provideStoreProductService(StoreProductDAO storeProductDAO){
        return new StoreProductService(storeProductDAO);
    }

}