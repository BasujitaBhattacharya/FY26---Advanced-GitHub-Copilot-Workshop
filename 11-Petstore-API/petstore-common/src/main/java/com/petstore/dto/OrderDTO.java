package com.petstore.dto;

import com.petstore.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing an order in the petstore system.
 */
public class OrderDTO {
    private Long id;
    private List<CartItemDTO> items;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public OrderDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the order's unique identifier
     * @param items the list of cart items in the order
     * @param totalPrice the total price of the order
     * @param status the current status of the order
     * @param createdAt the timestamp when the order was created
     */
    public OrderDTO(Long id, List<CartItemDTO> items, Double totalPrice, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Gets the order's unique identifier.
     *
     * @return the order ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the order's unique identifier.
     *
     * @param id the order ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the list of items in this order.
     *
     * @return the list of cart items
     */
    public List<CartItemDTO> getItems() {
        return items;
    }

    /**
     * Sets the list of items in this order.
     *
     * @param items the list of cart items to set
     */
    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    /**
     * Gets the total price of the order.
     *
     * @return the total price
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the current status of the order.
     *
     * @return the order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the order.
     *
     * @param status the order status to set
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Gets the timestamp when the order was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the order was created.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
