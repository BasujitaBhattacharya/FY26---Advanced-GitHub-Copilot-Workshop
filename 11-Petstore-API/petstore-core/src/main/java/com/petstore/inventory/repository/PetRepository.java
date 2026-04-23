package com.petstore.inventory.repository;

import com.petstore.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for Pet entities.
 * Provides CRUD operations and custom query methods for pet management.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * Finds all pets by their species.
     *
     * @param species the species name to search for
     * @return a list of pets matching the specified species
     */
    List<Pet> findBySpecies(String species);

    /**
     * Finds a pet by name (case-insensitive).
     *
     * @param name the pet name to search for
     * @return an Optional containing the pet if found
     */
    Optional<Pet> findByNameIgnoreCase(String name);

    /**
     * Finds all pets with inventory count greater than the specified amount.
     *
     * @param count the minimum inventory count
     * @return a list of pets with inventory count greater than the specified amount
     */
    List<Pet> findByInventoryCountGreaterThan(Integer count);
}
