package com.petstore.dto;

import com.petstore.Species;

/**
 * Data Transfer Object for a pet in the petstore system.
 */
public class PetDTO {
    private Long id;
    private String name;
    private Species species;
    private Double price;
    private Integer inventoryCount;

    /**
     * Default constructor.
     */
    public PetDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the pet's unique identifier
     * @param name the pet's name
     * @param species the pet's species
     * @param price the pet's price
     * @param inventoryCount the number of pets in stock
     */
    public PetDTO(Long id, String name, Species species, Double price, Integer inventoryCount) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.price = price;
        this.inventoryCount = inventoryCount;
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
     * Gets the pet's species.
     *
     * @return the species
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Sets the pet's species.
     *
     * @param species the species to set
     */
    public void setSpecies(Species species) {
        this.species = species;
    }

    /**
     * Gets the pet's price.
     *
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the pet's price.
     *
     * @param price the price to set
     */
    public void setPrice(Double price) {
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
        this.inventoryCount = inventoryCount;
    }

    @Override
    public String toString() {
        return "PetDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species=" + species +
                ", price=" + price +
                ", inventoryCount=" + inventoryCount +
                '}';
    }
}
