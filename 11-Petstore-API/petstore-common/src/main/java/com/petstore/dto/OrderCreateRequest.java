package com.petstore.dto;

import java.util.List;

/**
 * Request object for creating a new order.
 * Contains only the items to be ordered; server generates ID, timestamp, and status.
 */
public class OrderCreateRequest {
    private List<CartItemDTO> items;

    /**
     * Default constructor.
     */
    public OrderCreateRequest() {
    }

    /**
     * Constructor with items.
     *
     * @param items the list of cart items for the order
     */
    public OrderCreateRequest(List<CartItemDTO> items) {
        this.items = items;
    }

    /**
     * Gets the list of items for this order.
     *
     * @return the list of cart items
     */
    public List<CartItemDTO> getItems() {
        return items;
    }

    /**
     * Sets the list of items for this order.
     *
     * @param items the list of cart items to set
     */
    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderCreateRequest{" +
                "items=" + items +
                '}';
    }
}
