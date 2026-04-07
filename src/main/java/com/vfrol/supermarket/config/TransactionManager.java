package com.vfrol.supermarket.config;

import com.google.inject.Inject;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;

public class TransactionManager {
    private final Jdbi jdbi;

    @Inject
    public TransactionManager(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public <T, X extends Exception> T inTransaction(HandleCallback<T, X> callback) throws X {
        return jdbi.inTransaction(callback);
    }

    public <X extends Exception> void useTransaction(HandleConsumer<X> callback) throws X {
        jdbi.useTransaction(callback);
    }
}