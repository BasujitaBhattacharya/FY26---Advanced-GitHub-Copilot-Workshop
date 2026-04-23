package com.petstore.order.controller;

import com.petstore.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating an order's status.
 *
 * <p>This DTO is used in the PUT /api/orders/{id}/status endpoint to provide
 * the new status for an order.</p>
 */
public class UpdateOrderStatusRequest {

    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;

    /**
     * Default constructor.
     */
    public UpdateOrderStatusRequest() {
    }

    /**
     * Constructor with status field.
     *
     * @param status the new order status
     */
    public UpdateOrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    /**
     * Gets the new order status.
     *
     * @return the order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the new order status.
     *
     * @param status the order status to set
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateOrderStatusRequest{" +
                "status=" + status +
                '}';
    }
}
