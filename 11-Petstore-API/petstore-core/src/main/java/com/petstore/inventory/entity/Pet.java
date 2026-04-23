package com.petstore.inventory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity representing a pet in the petstore system.
 * Includes audit fields for creation and update timestamps, and version for optimistic locking.
 */
@Entity
@Table(name = "pets")
public class Pet {

    /**
     * The unique identifier for this pet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the pet. Must not be null and is limited to 100 characters.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * The species of the pet (DOG, CAT, BIRD, RABBIT, HAMSTER, FISH).
     */
    @Column(columnDefinition = "VARCHAR(50)")
    private String species;

    /**
     * The price of the pet. Must not be null and must be positive.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * The number of pets in inventory. Defaults to 0.
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer inventoryCount = 0;

    /**
     * Timestamp when the pet record was created. Automatically set by JPA auditing.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the pet record was last updated. Automatically updated by JPA auditing.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Version field for optimistic locking.
     */
    @Version
    private Long version;

    /**
     * Default constructor.
     */
    public Pet() {
    }

    /**
     * Constructor with pet details.
     *
     * @param name the pet's name
     * @param species the pet's species
     * @param price the pet's price
     * @param inventoryCount the number of pets in stock
     */
    public Pet(String name, String species, BigDecimal price, Integer inventoryCount) {
        this.name = name;
        this.species = species;
        this.price = price;
        this.inventoryCount = inventoryCount != null ? inventoryCount : 0;
    }

    /**
     * Gets the pet's unique identifier.
     *
     * @return the pet ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the pet's unique identifier.
     *
     * @param id the pet ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the pet's name.
     *
     * @return the pet name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the pet's name.
     *
     * @param name the pet name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the pet's species as a string.
     *
     * @return the species (DOG, CAT, BIRD, RABBIT, HAMSTER, FISH)
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Sets the pet's species as a string.
     *
     * @param species the species to set
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Gets the pet's price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the pet's price.
     *
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the inventory count for this pet.
     *
     * @return the inventory count
     */
    public Integer getInventoryCount() {
        return inventoryCount;
    }

    /**
     * Sets the inventory count for this pet.
     *
     * @param inventoryCount the inventory count to set
     */
    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount != null ? inventoryCount : 0;
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

    /**
     * Gets the last update timestamp.
     *
     * @return the update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt the update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the version for optimistic locking.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version for optimistic locking.
     *
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", price=" + price +
                ", inventoryCount=" + inventoryCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", version=" + version +
                '}';
    }
}
