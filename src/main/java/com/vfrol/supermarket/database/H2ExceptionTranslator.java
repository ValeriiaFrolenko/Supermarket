package com.vfrol.supermarket.database;

import com.google.inject.Singleton;
import com.vfrol.supermarket.exception.DataException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.jdbi.v3.core.statement.SqlExceptionHandler;

import java.sql.SQLException;

@Singleton
public class H2ExceptionTranslator implements SqlExceptionHandler {

    @Override
    public Throwable handle(SQLException e) {
        if (e instanceof JdbcSQLIntegrityConstraintViolationException sql) {
            DataException.Type type = switch (sql.getErrorCode()) {
                case 23505 -> DataException.Type.DUPLICATE;
                case 23503 -> DataException.Type.REFERENCE_IN_USE;
                case 23506, 23507 -> DataException.Type.REFERENCE_NOT_FOUND;
                case 23513 -> DataException.Type.CHECK_CONSTRAINT_VIOLATION;
                default -> DataException.Type.UNKNOWN;
            };

            String message = switch (type) {
                case DUPLICATE ->
                        "An item with this code or name already exists. Please use a different one.";

                case REFERENCE_IN_USE ->
                        "Cannot delete or modify this item because it is used in other records.\n" +
                                "Please remove or change the linked items first.";

                case REFERENCE_NOT_FOUND ->
                        "A referenced item (such as a category or product) does not exist.\n" +
                                "Please check your selection and try again.";

                case CHECK_CONSTRAINT_VIOLATION ->
                        "Some entered values are invalid.\n" +
                                "Please check all fields and correct the errors.";

                case UNKNOWN ->
                        "Database error occurred while saving data. Please try again.";
            };

            return new DataException(type, message, e);
        }

        return new DataException(DataException.Type.UNKNOWN,
                "Database error occurred. Please try again.", e);
    }
}