package com.petstore.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for REST endpoints.
 * Centralizes exception handling and error response formatting across the application.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PetNotFoundException.
     * Returns 404 Not Found status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFound(
            PetNotFoundException ex,
            HttpServletRequest request) {
        log.warn("Pet not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles CartItemNotFoundException.
     * Returns 404 Not Found status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartItemNotFound(
            CartItemNotFoundException ex,
            HttpServletRequest request) {
        log.warn("Cart item not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles OrderNotFoundException.
     * Returns 404 Not Found status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request) {
        log.warn("Order not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles InsufficientStockException and EmptyCartException.
     * Returns 400 Bad Request status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler({InsufficientStockException.class, EmptyCartException.class})
    public ResponseEntity<ErrorResponse> handleInvalidOperation(
            RuntimeException ex,
            HttpServletRequest request) {
        log.warn("Invalid operation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles InvalidPetException.
     * Returns 400 Bad Request status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(InvalidPetException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPet(
            InvalidPetException ex,
            HttpServletRequest request) {
        log.warn("Invalid pet data: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles InvalidOrderStatusException.
     * Returns 400 Bad Request status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStatus(
            InvalidOrderStatusException ex,
            HttpServletRequest request) {
        log.warn("Invalid order status: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles all generic exceptions not covered by specific handlers.
     * Returns 500 Internal Server Error status.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return error response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);
        return buildErrorResponse(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /**
     * Builds a standardized error response.
     *
     * @param message    the error message
     * @param status     the HTTP status
     * @param request    the HTTP request
     * @return the error response entity
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
