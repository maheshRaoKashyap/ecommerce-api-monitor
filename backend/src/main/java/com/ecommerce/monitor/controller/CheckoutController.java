package com.ecommerce.monitor.controller;

import com.ecommerce.monitor.metrics.EcommerceMetrics;
import com.ecommerce.monitor.model.Order;
import com.ecommerce.monitor.service.OrderService;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@Tag(name = "Checkout API", description = "Handles cart checkout and order placement")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final EcommerceMetrics metrics;
    private final OrderService orderService;

    public CheckoutController(EcommerceMetrics metrics, OrderService orderService) {
        this.metrics = metrics;
        this.orderService = orderService;
    }

    @PostMapping("/place-order")
    @Operation(summary = "Place a new order", description = "Processes checkout and creates an order record")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> payload) {
        metrics.recordCheckoutRequest();
        metrics.incrementActiveConnections();
        Timer.Sample sample = Timer.start();

        try {
            Order order = orderService.createOrder(payload);
            sample.stop(metrics.getCheckoutTimer());
            return ResponseEntity.ok(Map.of(
                "success", true,
                "orderId", order.getId(),
                "status", order.getStatus(),
                "responseTimeMs", order.getResponseTimeMs()
            ));
        } catch (Exception e) {
            metrics.recordCheckoutFailure();
            sample.stop(metrics.getCheckoutTimer());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } finally {
            metrics.decrementActiveConnections();
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Checkout service health check")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("service", "checkout", "status", "UP"));
    }
}
