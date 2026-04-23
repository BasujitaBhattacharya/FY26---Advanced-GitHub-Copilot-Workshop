package com.petstore.exception;

/**
 * Exception thrown when an invalid order status is encountered.
 */
public class InvalidOrderStatusException extends RuntimeException {

    /**
     * Constructs an InvalidOrderStatusException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidOrderStatusException(String message) {
        super(message);
    }

    /**
     * Constructs an InvalidOrderStatusException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public InvalidOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
