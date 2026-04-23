package com.petstore;

/**
 * Represents the status of an order in the petstore system.
 */
public enum OrderStatus {
    /**
     * Order has been placed but not yet processed.
     */
    PENDING,

    /**
     * Order is currently being processed.
     */
    PROCESSING,

    /**
     * Order has been completed and ready for pickup/delivery.
     */
    COMPLETED,

    /**
     * Order has been cancelled.
     */
    CANCELLED
}
