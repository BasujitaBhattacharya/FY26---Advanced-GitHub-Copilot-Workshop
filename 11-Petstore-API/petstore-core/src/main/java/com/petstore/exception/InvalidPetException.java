package com.petstore.exception;

/**
 * Exception thrown when a pet's data is invalid.
 * This includes cases such as invalid price or missing required fields.
 */
public class InvalidPetException extends RuntimeException {

    /**
     * Constructs a new InvalidPetException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPetException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPetException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidPetException(String message, Throwable cause) {
        super(message, cause);
    }
}
