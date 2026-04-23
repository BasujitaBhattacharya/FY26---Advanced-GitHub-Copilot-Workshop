package com.petstore.order.exception;

/**
 * Exception thrown when attempting to create an order from an empty cart.
 */
public class EmptyCartException extends Exception {

    /**
     * Constructs a new EmptyCartException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmptyCartException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmptyCartException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}
