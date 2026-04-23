package com.petstore.exception;

/**
 * Exception thrown when a requested cart item is not found.
 */
public class CartItemNotFoundException extends RuntimeException {

    /**
     * Constructs a CartItemNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CartItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a CartItemNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public CartItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
