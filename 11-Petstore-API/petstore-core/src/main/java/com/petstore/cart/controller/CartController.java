package com.petstore.cart.controller;

import com.petstore.cart.dto.AddCartItemRequest;
import com.petstore.cart.dto.CartDTO;
import com.petstore.cart.dto.CartItemDTO;
import com.petstore.cart.service.CartService;
import com.petstore.exception.CartItemNotFoundException;
import com.petstore.exception.InsufficientStockException;
import com.petstore.exception.PetNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing shopping cart operations.
 * Provides endpoints to add, remove, update, and retrieve items in the shopping cart.
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "Shopping Cart", description = "APIs for managing the shopping cart")
public class CartController {

    private final CartService cartService;

    /**
     * Constructs a CartController with the specified CartService.
     *
     * @param cartService the service for cart operations
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Retrieves the current shopping cart with all items and total price.
     *
     * @return a ResponseEntity containing the CartDTO with status code 200 OK
     */
    @GetMapping
    @Operation(
        summary = "Get current cart",
        description = "Retrieves the current shopping cart with all items and the total price"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Cart retrieved successfully",
        content = @Content(schema = @Schema(implementation = CartDTO.class))
    )
    public ResponseEntity<CartDTO> getCart() {
        List<CartItemDTO> cartItems = cartService.getCart();
        BigDecimal total = cartService.calculateCartTotal();
        CartDTO cartDTO = new CartDTO(cartItems, total);
        return ResponseEntity.ok(cartDTO);
    }

    /**
     * Adds an item to the shopping cart.
     * Validates that the pet exists and sufficient stock is available.
     *
     * @param request the request containing pet ID and quantity to add
     * @return a ResponseEntity containing the added CartItemDTO with status code 201 CREATED
     * @throws PetNotFoundException if the pet is not found
     * @throws InsufficientStockException if there is insufficient stock for the requested quantity
     * @throws IllegalArgumentException if the quantity is invalid
     */
    @PostMapping("/items")
    @Operation(
        summary = "Add item to cart",
        description = "Adds a pet item to the shopping cart with the specified quantity"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Item added to cart successfully",
            content = @Content(schema = @Schema(implementation = CartItemDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
        @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    public ResponseEntity<CartItemDTO> addItemToCart(@RequestBody @Valid AddCartItemRequest request) {
        CartItemDTO cartItem = cartService.addItemToCart(request.getPetId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    /**
     * Updates the quantity of an item in the shopping cart.
     *
     * @param cartItemId the ID of the cart item to update
     * @param quantity the new quantity for the item
     * @return a ResponseEntity containing the updated CartItemDTO with status code 200 OK
     * @throws CartItemNotFoundException if the cart item is not found
     * @throws InsufficientStockException if there is insufficient stock for the new quantity
     * @throws IllegalArgumentException if the quantity is invalid
     */
    @PutMapping("/items/{cartItemId}")
    @Operation(
        summary = "Update item quantity",
        description = "Updates the quantity of an item already in the shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item quantity updated successfully",
            content = @Content(schema = @Schema(implementation = CartItemDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient stock"),
        @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<CartItemDTO> updateItemQuantity(
            @PathVariable
            @Parameter(description = "The ID of the cart item to update")
            Long cartItemId,
            @RequestParam
            @Parameter(description = "The new quantity for the item")
            Integer quantity) {
        CartItemDTO updatedItem = cartService.updateItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Removes an item from the shopping cart.
     *
     * @param cartItemId the ID of the cart item to remove
     * @return a ResponseEntity with status code 204 NO CONTENT
     * @throws CartItemNotFoundException if the cart item is not found
     */
    @DeleteMapping("/items/{cartItemId}")
    @Operation(
        summary = "Remove item from cart",
        description = "Removes a specific item from the shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Item removed from cart successfully"
        ),
        @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable
            @Parameter(description = "The ID of the cart item to remove")
            Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clears all items from the shopping cart.
     *
     * @return a ResponseEntity with status code 204 NO CONTENT
     */
    @DeleteMapping
    @Operation(
        summary = "Clear cart",
        description = "Removes all items from the shopping cart"
    )
    @ApiResponse(
        responseCode = "204",
        description = "Cart cleared successfully"
    )
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the total price of all items in the shopping cart.
     *
     * @return a ResponseEntity containing the total price as a BigDecimal with status code 200 OK
     */
    @GetMapping("/total")
    @Operation(
        summary = "Get cart total",
        description = "Calculates and returns the total price of all items in the shopping cart"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Cart total calculated successfully",
        content = @Content(schema = @Schema(implementation = BigDecimal.class))
    )
    public ResponseEntity<BigDecimal> getCartTotal() {
        BigDecimal total = cartService.calculateCartTotal();
        return ResponseEntity.ok(total);
    }
}
