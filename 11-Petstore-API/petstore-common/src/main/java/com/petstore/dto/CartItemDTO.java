package com.petstore.dto;

/**
 * Data Transfer Object representing an item in a shopping cart.
 */
public class CartItemDTO {
    private Long id;
    private Long petId;
    private Integer quantity;

    /**
     * Default constructor.
     */
    public CartItemDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the cart item's unique identifier
     * @param petId the ID of the pet in the cart
     * @param quantity the quantity of this pet in the cart
     */
    public CartItemDTO(Long id, Long petId, Integer quantity) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
    }

    /**
     * Gets the cart item's unique identifier.
     *
     * @return the cart item ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the cart item's unique identifier.
     *
     * @param id the cart item ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the pet in this cart item.
     *
     * @return the pet ID
     */
    public Long getPetId() {
        return petId;
    }

    /**
     * Sets the ID of the pet in this cart item.
     *
     * @param petId the pet ID to set
     */
    public void setPetId(Long petId) {
        this.petId = petId;
    }

    /**
     * Gets the quantity of pets in this cart item.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of pets in this cart item.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                '}';
    }
}
