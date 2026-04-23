package com.petstore.cart.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for the shopping cart.
 * Contains a list of cart items and the total price of the cart.
 */
public class CartDTO {

    private List<CartItemDTO> items;
    private BigDecimal total;

    /**
     * Default constructor initializing an empty cart.
     */
    public CartDTO() {
        this.items = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    /**
     * Constructor with cart items and total.
     *
     * @param items the list of cart items
     * @param total the total price of the cart
     */
    public CartDTO(List<CartItemDTO> items, BigDecimal total) {
        this.items = items != null ? items : new ArrayList<>();
        this.total = total != null ? total : BigDecimal.ZERO;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Gets the number of items in the cart.
     *
     * @return the count of cart items
     */
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if the cart has no items, false otherwise
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "items=" + items +
                ", total=" + total +
                '}';
    }
}
