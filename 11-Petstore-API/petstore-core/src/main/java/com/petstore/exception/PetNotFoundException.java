package com.petstore.exception;

/**
 * Exception thrown when a pet is not found in the system.
 * This occurs when attempting to retrieve, update, or delete a pet by ID that does not exist.
 */
public class PetNotFoundException extends RuntimeException {

    /**
     * Constructs a new PetNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PetNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new PetNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public PetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
