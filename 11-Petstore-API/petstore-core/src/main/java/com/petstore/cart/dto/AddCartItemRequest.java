package com.petstore.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for adding an item to the shopping cart.
 * Contains the pet ID and desired quantity for the cart operation.
 */
public class AddCartItemRequest {

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    /**
     * Default constructor.
     */
    public AddCartItemRequest() {
    }

    /**
     * Constructor with pet ID and quantity.
     *
     * @param petId the ID of the pet to add to the cart
     * @param quantity the quantity to add
     */
    public AddCartItemRequest(Long petId, Integer quantity) {
        this.petId = petId;
        this.quantity = quantity;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "AddCartItemRequest{" +
                "petId=" + petId +
                ", quantity=" + quantity +
                '}';
    }
}
