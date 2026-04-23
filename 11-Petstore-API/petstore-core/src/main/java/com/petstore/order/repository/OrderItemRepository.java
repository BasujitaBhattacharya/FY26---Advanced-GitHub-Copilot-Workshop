package com.petstore.order.repository;

import com.petstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository interface for OrderItem entities.
 * Provides CRUD operations and custom query methods for order item management.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all items associated with a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of order items for the specified order
     */
    List<OrderItem> findByOrderId(Long orderId);
}
