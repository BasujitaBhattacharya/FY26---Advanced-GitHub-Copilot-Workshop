package com.petstore.cart.service;

import com.petstore.cart.dto.CartItemDTO;
import com.petstore.cart.repository.CartItemRepository;
import com.petstore.exception.CartItemNotFoundException;
import com.petstore.exception.InsufficientStockException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.inventory.repository.PetRepository;
import com.petstore.inventory.service.PetInventoryService;
import com.petstore.model.CartItem;
import com.petstore.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing shopping cart operations in the petstore system.
 * Provides functionality to add, remove, and manage items in a shopping cart.
 * All database operations are transactional for data consistency.
 */
@Service
@Transactional
public class CartService {

    private static final String INVALID_QUANTITY_MESSAGE = "Quantity must be greater than 0";
    private static final String PET_NOT_FOUND_MESSAGE = "Pet not found with id: ";
    private static final String CART_ITEM_NOT_FOUND_MESSAGE = "Cart item not found with id: ";
    private static final String INSUFFICIENT_STOCK_MESSAGE = "Insufficient stock for pet id: ";

    private final CartItemRepository cartItemRepository;
    private final PetRepository petRepository;
    private final PetInventoryService petInventoryService;

    /**
     * Constructs a CartService with the specified repositories and services.
     *
     * @param cartItemRepository the repository for cart item data access
     * @param petRepository the repository for pet data access
     * @param petInventoryService the service for pet inventory operations
     */
    public CartService(CartItemRepository cartItemRepository, PetRepository petRepository,
                       PetInventoryService petInventoryService) {
        this.cartItemRepository = cartItemRepository;
        this.petRepository = petRepository;
        this.petInventoryService = petInventoryService;
    }

    /**
     * Adds an item to the shopping cart.
     * Validates that the pet exists, quantity is positive, and sufficient stock is available.
     *
     * @param petId the ID of the pet to add to the cart
     * @param quantity the quantity to add to the cart
     * @return the added cart item as a CartItemDTO
     * @throws PetNotFoundException if the pet is not found
     * @throws InsufficientStockException if there is insufficient stock for the requested quantity
     * @throws IllegalArgumentException if the quantity is not positive
     */
    public CartItemDTO addItemToCart(Long petId, Integer quantity)
            throws PetNotFoundException, InsufficientStockException {
        validateQuantity(quantity);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + petId));

        if (pet.getInventoryCount() < quantity) {
            throw new InsufficientStockException(
                    INSUFFICIENT_STOCK_MESSAGE + petId + ". Available: " + pet.getInventoryCount()
                            + ", Requested: " + quantity
            );
        }

        CartItem cartItem = new CartItem(pet, quantity);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return convertToDTO(savedCartItem);
    }

    /**
     * Removes an item from the shopping cart.
     * The item is identified by its cart item ID.
     *
     * @param cartItemId the ID of the cart item to remove
     * @throws CartItemNotFoundException if the cart item is not found
     */
    public void removeItemFromCart(Long cartItemId) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(CART_ITEM_NOT_FOUND_MESSAGE + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    /**
     * Retrieves all items currently in the shopping cart.
     *
     * @return a list of all cart items as CartItemDTOs
     */
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCart() {
        return cartItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total price of all items in the shopping cart.
     * The total is computed by summing (pet.price * quantity) for each cart item.
     *
     * @return the total price of the cart as a BigDecimal
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal() {
        return cartItemRepository.findAll().stream()
                .map(cartItem -> cartItem.getPet().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Clears all items from the shopping cart.
     * Deletes all cart items from the database.
     */
    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    /**
     * Updates the quantity of an item in the shopping cart.
     * Validates that the new quantity is positive and that sufficient stock is available.
     *
     * @param cartItemId the ID of the cart item to update
     * @param quantity the new quantity for the cart item
     * @return the updated cart item as a CartItemDTO
     * @throws CartItemNotFoundException if the cart item is not found
     * @throws InsufficientStockException if there is insufficient stock for the new quantity
     * @throws IllegalArgumentException if the quantity is not positive
     */
    public CartItemDTO updateItemQuantity(Long cartItemId, Integer quantity)
            throws CartItemNotFoundException, InsufficientStockException {
        validateQuantity(quantity);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(CART_ITEM_NOT_FOUND_MESSAGE + cartItemId));

        Pet pet = cartItem.getPet();
        if (pet.getInventoryCount() < quantity) {
            throw new InsufficientStockException(
                    INSUFFICIENT_STOCK_MESSAGE + pet.getId() + ". Available: " + pet.getInventoryCount()
                            + ", Requested: " + quantity
            );
        }

        cartItem.setQuantity(quantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        return convertToDTO(updatedCartItem);
    }

    /**
     * Validates that the quantity is positive.
     *
     * @param quantity the quantity to validate
     * @throws IllegalArgumentException if the quantity is not positive
     */
    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(INVALID_QUANTITY_MESSAGE);
        }
    }

    /**
     * Converts a CartItem entity to a CartItemDTO.
     * Computes the item total by multiplying pet price by quantity.
     *
     * @param cartItem the CartItem entity
     * @return the converted CartItemDTO
     */
    private CartItemDTO convertToDTO(CartItem cartItem) {
        Pet pet = cartItem.getPet();
        BigDecimal itemTotal = pet.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemDTO(
                cartItem.getId(),
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getPrice(),
                cartItem.getQuantity(),
                itemTotal
        );
    }
}
