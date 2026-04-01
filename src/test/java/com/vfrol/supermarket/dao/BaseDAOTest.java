package com.vfrol.supermarket.dao;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseDAOTest {

    protected Handle handle;

    @BeforeEach
    void setUpDatabase() throws IOException {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        handle = jdbi.open();
        handle.setSqlLogger(new Slf4JSqlLogger());

        var inputStream = getClass().getResourceAsStream("/sql/schema.sql");
        assertNotNull(inputStream, "Resource not found: /sql/schema.sql");
        String schemaSql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        handle.createScript(schemaSql).execute();
    }

    @AfterEach
    void tearDownDatabase() {
        if (handle != null && !handle.isClosed()) {
            handle.close();
        }
    }
}