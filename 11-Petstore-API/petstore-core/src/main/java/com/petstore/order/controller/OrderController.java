package com.petstore.order.controller;

import com.petstore.OrderStatus;
import com.petstore.dto.OrderDTO;
import com.petstore.order.exception.InvalidOrderStatusException;
import com.petstore.order.exception.OrderNotFoundException;
import com.petstore.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing orders in the Petstore API.
 *
 * <p>This controller provides endpoints to create, retrieve, update, and cancel orders.
 * All operations support standard HTTP status codes and comprehensive error handling.</p>
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for managing Petstore orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructs an OrderController with the required OrderService dependency.
     *
     * @param orderService the service for order operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders in the system, ordered by creation date (most recent first).
     *
     * @return ResponseEntity containing a list of all OrderDTOs with HTTP 200 status
     */
    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieves all orders in the system, sorted by creation date (most recent first)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)))
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves a specific order by its unique identifier.
     *
     * @param id the order's unique identifier
     * @return ResponseEntity containing the OrderDTO with HTTP 200 status
     * @throws OrderNotFoundException if the order does not exist
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Creates a new order from items currently in the shopping cart.
     *
     * <p>This operation atomically creates an order with PENDING status, decrements
     * inventory for each item, and clears the shopping cart.</p>
     *
     * @return ResponseEntity containing the newly created OrderDTO with HTTP 201 status
     * @throws com.petstore.order.exception.EmptyCartException if the cart is empty
     * @throws com.petstore.order.exception.InsufficientStockException if any item lacks sufficient inventory
     */
    @PostMapping
    @Operation(summary = "Create order from cart", description = "Creates a new order from items in the shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Cart is empty or insufficient stock available")
    })
    public ResponseEntity<OrderDTO> createOrder() {
        OrderDTO order = orderService.createOrderFromCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status the order status to filter by (PENDING, PROCESSING, COMPLETED, or CANCELLED)
     * @return ResponseEntity containing a list of OrderDTOs with the specified status and HTTP 200 status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieves all orders with a specific status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders with the specified status",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)))
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates the status of an existing order.
     *
     * <p>Valid status transitions:
     * <ul>
     *   <li>PENDING → PROCESSING or CANCELLED</li>
     *   <li>PROCESSING → COMPLETED or CANCELLED</li>
     *   <li>COMPLETED or CANCELLED → no further transitions allowed</li>
     * </ul>
     * </p>
     *
     * @param id the order's unique identifier
     * @param request the request body containing the new status
     * @return ResponseEntity containing the updated OrderDTO with HTTP 200 status
     * @throws OrderNotFoundException if the order does not exist
     * @throws InvalidOrderStatusException if the status transition is invalid
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status of an existing order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) throws OrderNotFoundException, InvalidOrderStatusException {
        OrderDTO order = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(order);
    }

    /**
     * Cancels an existing order.
     *
     * <p>An order can only be cancelled if it is in PENDING or PROCESSING status.
     * Upon cancellation, the ordered pet quantities are restored to inventory.</p>
     *
     * @param id the order's unique identifier
     * @return ResponseEntity with HTTP 204 (No Content) status on successful cancellation
     * @throws OrderNotFoundException if the order does not exist
     * @throws InvalidOrderStatusException if the order cannot be cancelled from its current status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel order", description = "Cancels an existing order and restores inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled from current status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) throws OrderNotFoundException, InvalidOrderStatusException {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
