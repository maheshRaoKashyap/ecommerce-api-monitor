package com.ecommerce.monitor.service;

import com.ecommerce.monitor.model.Order;
import com.ecommerce.monitor.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Map<String, Object> payload) {
        long start = System.currentTimeMillis();
        Order order = new Order();
        order.setUserId((String) payload.getOrDefault("userId", "anon"));
        order.setTotalAmount(Double.parseDouble(payload.getOrDefault("amount", "0").toString()));
        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setResponseTimeMs(System.currentTimeMillis() - start);
        return orderRepository.save(order);
    }
}
