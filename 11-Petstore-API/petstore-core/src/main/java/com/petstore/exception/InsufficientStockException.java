package com.petstore.exception;

/**
 * Exception thrown when there is insufficient stock to complete an operation.
 * This occurs when attempting to decrement inventory below zero or to an insufficient level.
 */
public class InsufficientStockException extends RuntimeException {

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
