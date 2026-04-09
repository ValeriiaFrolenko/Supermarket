package com.vfrol.supermarket.service.validator;

import java.util.Optional;

public abstract class BaseValidator {

    protected <T> void requireNotExists(Optional<T> found, String message) {
        if (found.isPresent()) {
            throw new ValidationException(message);
        }
    }

    protected <T> void requireExists(Optional<T> found, String message) {
        if (found.isEmpty()) {
            throw new ValidationException(message);
        }
    }
}