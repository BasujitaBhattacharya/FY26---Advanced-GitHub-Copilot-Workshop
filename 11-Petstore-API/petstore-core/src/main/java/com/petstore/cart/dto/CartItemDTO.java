package com.petstore.cart.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for cart items.
 * Represents a pet in a shopping cart with quantity and item total.
 */
public class CartItemDTO {

    private Long id;
    private Long petId;
    private String petName;
    private String species;
    private BigDecimal petPrice;
    private Integer quantity;
    private BigDecimal itemTotal;

    /**
     * Default constructor.
     */
    public CartItemDTO() {
    }

    /**
     * Constructor with cart item details.
     *
     * @param id the cart item ID
     * @param petId the pet ID
     * @param petName the pet name
     * @param species the pet species
     * @param petPrice the pet price
     * @param quantity the quantity in cart
     * @param itemTotal the total price for this item (petPrice * quantity)
     */
    public CartItemDTO(Long id, Long petId, String petName, String species, BigDecimal petPrice, Integer quantity, BigDecimal itemTotal) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.species = species;
        this.petPrice = petPrice;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public BigDecimal getPetPrice() {
        return petPrice;
    }

    public void setPetPrice(BigDecimal petPrice) {
        this.petPrice = petPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", petId=" + petId +
                ", petName='" + petName + '\'' +
                ", species='" + species + '\'' +
                ", petPrice=" + petPrice +
                ", quantity=" + quantity +
                ", itemTotal=" + itemTotal +
                '}';
    }
}
