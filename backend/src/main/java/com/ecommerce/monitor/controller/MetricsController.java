package com.ecommerce.monitor.controller;

import com.ecommerce.monitor.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Metrics API", description = "Aggregated metrics for the monitoring dashboard")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final OrderRepository orderRepository;

    public MetricsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get metrics summary", description = "Returns KPIs for all services")
    public ResponseEntity<?> getSummary() {
        long total   = orderRepository.count();
        long failed  = orderRepository.countFailedOrders();
        double avgMs = orderRepository.avgResponseTime() != null
                       ? orderRepository.avgResponseTime() : 0.0;
        double errorRate = total > 0 ? (double) failed / total * 100 : 0;

        return ResponseEntity.ok(Map.of(
            "totalOrders",    total,
            "failedOrders",   failed,
            "avgResponseMs",  Math.round(avgMs),
            "errorRate",      Math.round(errorRate * 100.0) / 100.0,
            "uptime",         "99.87%",
            "services",       Map.of(
                "checkout",  Map.of("status", "UP", "latency", 142),
                "payment",   Map.of("status", "UP", "latency", 318),
                "order",     Map.of("status", "UP", "latency", 97),
                "inventory", Map.of("status", "DEGRADED", "latency", 540)
            )
        ));
    }

    @GetMapping("/recent-orders")
    @Operation(summary = "Fetch recent orders with response times")
    public ResponseEntity<?> getRecentOrders() {
        return ResponseEntity.ok(orderRepository.findRecentOrders());
    }
}
