package com.petstore.order.service;

import com.petstore.dto.CartItemDTO;
import com.petstore.dto.OrderDTO;
import com.petstore.model.CartItem;
import com.petstore.model.Order;
import com.petstore.model.OrderItem;
import com.petstore.model.OrderStatus;
import com.petstore.model.Pet;
import com.petstore.order.exception.EmptyCartException;
import com.petstore.order.exception.InsufficientStockException;
import com.petstore.order.exception.InvalidOrderStatusException;
import com.petstore.order.exception.OrderNotFoundException;
import com.petstore.order.repository.OrderItemRepository;
import com.petstore.order.repository.OrderRepository;
import com.petstore.cart.repository.CartItemRepository;
import com.petstore.inventory.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing orders in the Petstore system.
 *
 * <p>This service handles order creation from cart items, order retrieval, status updates,
 * and order cancellation. All multi-step operations are managed within transactions to ensure
 * data consistency.</p>
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final PetRepository petRepository;

    /**
     * Constructs an OrderService with required dependencies.
     *
     * @param orderRepository the repository for order persistence
     * @param orderItemRepository the repository for order item persistence
     * @param cartItemRepository the repository for cart item persistence
     * @param petRepository the repository for pet inventory
     */
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        CartItemRepository cartItemRepository, PetRepository petRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.petRepository = petRepository;
    }

    /**
     * Creates a new order from the items currently in the shopping cart.
     *
     * <p>This operation performs the following steps atomically:
     * <ol>
     *   <li>Fetches all items from the shopping cart</li>
     *   <li>Validates that the cart is not empty</li>
     *   <li>Validates that all items have sufficient inventory stock</li>
     *   <li>Creates a new Order with status PENDING</li>
     *   <li>Creates OrderItems with the pet's current price at time of order</li>
     *   <li>Decrements the pet inventory for each item ordered</li>
     *   <li>Clears all items from the shopping cart</li>
     *   <li>Calculates and sets the total order price</li>
     *   <li>Persists and returns the new Order as OrderDTO</li>
     * </ol>
     * </p>
     *
     * @return OrderDTO representing the newly created order
     * @throws EmptyCartException if the cart contains no items
     * @throws InsufficientStockException if any ordered pet lacks sufficient inventory
     */
    @Transactional
    public OrderDTO createOrderFromCart() throws EmptyCartException, InsufficientStockException {
        // Fetch all cart items
        List<CartItem> cartItems = cartItemRepository.findAll();

        // Validate cart is not empty
        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cannot create order from empty cart");
        }

        // Validate all items have sufficient stock
        for (CartItem cartItem : cartItems) {
            Pet pet = cartItem.getPet();
            if (pet.getInventoryCount() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                    String.format("Insufficient stock for pet '%s': requested %d, available %d",
                        pet.getName(), cartItem.getQuantity(), pet.getInventoryCount())
                );
            }
        }

        // Create Order with status PENDING
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setItems(new ArrayList<>());

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Create OrderItems and update inventory
        for (CartItem cartItem : cartItems) {
            Pet pet = cartItem.getPet();

            // Create OrderItem with current pet price
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPet(pet);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(pet.getPrice());

            order.getItems().add(orderItem);

            // Calculate item total
            BigDecimal itemTotal = pet.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);

            // Decrement pet inventory
            pet.setInventoryCount(pet.getInventoryCount() - cartItem.getQuantity());
            petRepository.save(pet);
        }

        // Set order total price
        order.setTotalPrice(totalPrice);

        // Persist the order
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteAll();

        // Return OrderDTO
        return mapOrderToDTO(savedOrder);
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the order's unique identifier
     * @return OrderDTO representing the requested order
     * @throws OrderNotFoundException if no order exists with the specified ID
     */
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format("Order with ID %d not found", id)
            ));
        return mapOrderToDTO(order);
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return a list of all OrderDTOs, ordered by creation date (most recent first)
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(this::mapOrderToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status the order status to filter by
     * @return a list of OrderDTOs matching the specified status
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
            .map(this::mapOrderToDTO)
            .collect(Collectors.toList());
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
     * @param newStatus the new order status
     * @return OrderDTO representing the updated order
     * @throws OrderNotFoundException if no order exists with the specified ID
     * @throws InvalidOrderStatusException if the status transition is not allowed
     */
    @Transactional
    public OrderDTO updateOrderStatus(Long id, OrderStatus newStatus)
            throws OrderNotFoundException, InvalidOrderStatusException {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format("Order with ID %d not found", id)
            ));

        OrderStatus currentStatus = order.getStatus();

        // Validate status transition
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusException(
                String.format("Cannot transition from %s to %s", currentStatus, newStatus)
            );
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapOrderToDTO(updatedOrder);
    }

    /**
     * Cancels an existing order.
     *
     * <p>An order can only be cancelled if it is in PENDING or PROCESSING status.
     * Upon cancellation, the ordered pet quantities are restored to inventory.</p>
     *
     * @param id the order's unique identifier
     * @throws OrderNotFoundException if no order exists with the specified ID
     * @throws InvalidOrderStatusException if the order cannot be cancelled from its current status
     */
    @Transactional
    public void cancelOrder(Long id) throws OrderNotFoundException, InvalidOrderStatusException {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format("Order with ID %d not found", id)
            ));

        OrderStatus currentStatus = order.getStatus();

        // Only PENDING and PROCESSING orders can be cancelled
        if (currentStatus != OrderStatus.PENDING && currentStatus != OrderStatus.PROCESSING) {
            throw new InvalidOrderStatusException(
                String.format("Cannot cancel order with status %s", currentStatus)
            );
        }

        // Restore inventory for each order item
        for (OrderItem orderItem : order.getItems()) {
            Pet pet = (Pet) orderItem.getPet();
            pet.setInventoryCount(pet.getInventoryCount() + orderItem.getQuantity());
            petRepository.save(pet);
        }

        // Update order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    /**
     * Maps an Order entity to an OrderDTO.
     *
     * @param order the Order entity to map
     * @return the corresponding OrderDTO
     */
    private OrderDTO mapOrderToDTO(Order order) {
        List<CartItemDTO> itemDTOs = order.getItems().stream()
            .map(item -> {
                Pet pet = (Pet) item.getPet();
                return new CartItemDTO(item.getId(), pet.getId(), item.getQuantity());
            })
            .collect(Collectors.toList());

        // Convert OrderStatus from model to common
        com.petstore.OrderStatus commonStatus = com.petstore.OrderStatus.valueOf(order.getStatus().name());

        return new OrderDTO(
            order.getId(),
            itemDTOs,
            order.getTotalPrice().doubleValue(),
            commonStatus,
            order.getCreatedAt()
        );
    }

    /**
     * Validates whether a status transition is allowed.
     *
     * @param currentStatus the current order status
     * @param newStatus the desired new order status
     * @return true if the transition is valid, false otherwise
     */
    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Cannot transition from the same status
        if (currentStatus == newStatus) {
            return false;
        }

        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING:
                return newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}
