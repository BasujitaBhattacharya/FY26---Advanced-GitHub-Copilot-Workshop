package com.petstore.inventory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity representing an item in a shopping cart.
 * Associates a pet with a quantity in a cart.
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    /**
     * The unique identifier for this cart item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The pet associated with this cart item. Must not be null.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    /**
     * The quantity of this pet in the cart. Must not be null and must be positive.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Timestamp when the cart item was created. Automatically set by JPA auditing.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public CartItem() {
    }

    /**
     * Constructor with cart item details.
     *
     * @param pet the pet in the cart
     * @param quantity the quantity of the pet
     */
    public CartItem(Pet pet, Integer quantity) {
        this.pet = pet;
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
     * Gets the pet in this cart item.
     *
     * @return the pet
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the pet in this cart item.
     *
     * @param pet the pet to set
     */
    public void setPet(Pet pet) {
        this.pet = pet;
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

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", pet=" + (pet != null ? pet.getId() : null) +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }
}
