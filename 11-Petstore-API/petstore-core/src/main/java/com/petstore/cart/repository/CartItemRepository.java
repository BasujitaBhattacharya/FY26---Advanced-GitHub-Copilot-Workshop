package com.petstore.cart.repository;

import com.petstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for CartItem entities.
 * Provides CRUD operations and custom query methods for shopping cart item management.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Finds all cart items associated with a specific pet.
     *
     * @param petId the ID of the pet
     * @return a list of cart items for the specified pet
     */
    List<CartItem> findByPetId(Long petId);

    /**
     * Deletes all cart items associated with a specific pet.
     *
     * @param petId the ID of the pet
     */
    void deleteByPetId(Long petId);

    /**
     * Finds a specific cart item by both pet ID and cart item ID.
     *
     * @param petId the ID of the pet
     * @param id the ID of the cart item
     * @return an Optional containing the cart item if found
     */
    Optional<CartItem> findByPetIdAndId(Long petId, Long id);
}
