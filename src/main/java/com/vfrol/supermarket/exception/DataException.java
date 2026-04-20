package com.vfrol.supermarket.exception;

import lombok.Getter;

@Getter
public class DataException extends RuntimeException {

    public enum Type {
        DUPLICATE,
        REFERENCE_IN_USE,
        REFERENCE_NOT_FOUND,
        CHECK_CONSTRAINT_VIOLATION,
        UNKNOWN
    }

    private final Type type;

    public DataException(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}