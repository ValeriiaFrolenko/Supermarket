package com.vfrol.supermarket.database;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlExceptionHandler;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;

public class JdbiFactory {

    private final SqlExceptionHandler exceptionTranslator;

    public JdbiFactory(SqlExceptionHandler exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    public Jdbi create(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);

        jdbi.getConfig(SqlStatements.class).addExceptionHandler(exceptionTranslator);

        return jdbi;
    }
}