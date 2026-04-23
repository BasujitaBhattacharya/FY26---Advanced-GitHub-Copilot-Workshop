package com.petstore.exception;

/**
 * Exception thrown when a requested order is not found.
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Constructs an OrderNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an OrderNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
