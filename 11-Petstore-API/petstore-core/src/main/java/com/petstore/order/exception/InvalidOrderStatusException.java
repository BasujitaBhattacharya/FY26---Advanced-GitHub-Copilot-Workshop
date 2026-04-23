package com.petstore.order.exception;

/**
 * Exception thrown when an invalid order status transition is attempted.
 */
public class InvalidOrderStatusException extends Exception {

    /**
     * Constructs a new InvalidOrderStatusException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidOrderStatusException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidOrderStatusException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
