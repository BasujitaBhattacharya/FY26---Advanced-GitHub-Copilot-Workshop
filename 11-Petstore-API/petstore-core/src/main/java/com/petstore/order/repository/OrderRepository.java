package com.petstore.order.repository;

import com.petstore.model.Order;
import com.petstore.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository interface for Order entities.
 * Provides CRUD operations and custom query methods for order management.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders with a specific status.
     *
     * @param status the order status to search for
     * @return a list of orders matching the specified status
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Finds all orders sorted by creation date in descending order (most recent first).
     *
     * @return a list of all orders ordered by creation date in descending order
     */
    List<Order> findAllByOrderByCreatedAtDesc();
}
