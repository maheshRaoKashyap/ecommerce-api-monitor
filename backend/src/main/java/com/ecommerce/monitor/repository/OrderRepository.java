package com.ecommerce.monitor.repository;

import com.ecommerce.monitor.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'FAILED'")
    Long countFailedOrders();

    @Query("SELECT AVG(o.responseTimeMs) FROM Order o")
    Double avgResponseTime();

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders();
}
