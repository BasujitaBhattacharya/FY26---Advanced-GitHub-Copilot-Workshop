package com.petstore.order.exception;

/**
 * Exception thrown when attempting to purchase a pet but insufficient stock is available.
 */
public class InsufficientStockException extends Exception {

    /**
     * Constructs a new InsufficientStockException with the specified detail message.
     *
     * @param message the detail message
     */
    public InsufficientStockException(String message) {
        super(message);
    }

    /**
     * Constructs a new InsufficientStockException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
