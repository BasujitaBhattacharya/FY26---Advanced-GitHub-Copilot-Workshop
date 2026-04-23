package com.petstore.exception;

/**
 * Exception thrown when attempting to perform an operation on an empty cart.
 */
public class EmptyCartException extends RuntimeException {

    /**
     * Constructs an EmptyCartException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmptyCartException(String message) {
        super(message);
    }

    /**
     * Constructs an EmptyCartException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}
