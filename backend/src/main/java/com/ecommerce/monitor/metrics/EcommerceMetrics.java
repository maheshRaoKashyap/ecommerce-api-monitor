package com.ecommerce.monitor.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EcommerceMetrics {

    private final Counter checkoutRequests;
    private final Counter checkoutFailures;
    private final Counter paymentRequests;
    private final Counter paymentFailures;
    private final Counter orderRequests;
    private final Counter orderFailures;
    private final Counter inventoryRequests;
    private final Counter inventoryFailures;
    private final Timer checkoutTimer;
    private final Timer paymentTimer;
    private final Timer orderTimer;
    private final Timer inventoryTimer;
    private final AtomicInteger activeConnections = new AtomicInteger(0);

    public EcommerceMetrics(MeterRegistry registry) {
        // Request counters per service
        this.checkoutRequests  = Counter.builder("ecommerce.checkout.requests.total")
                .description("Total checkout API requests").register(registry);
        this.checkoutFailures  = Counter.builder("ecommerce.checkout.failures.total")
                .description("Total checkout API failures").register(registry);
        this.paymentRequests   = Counter.builder("ecommerce.payment.requests.total")
                .description("Total payment API requests").register(registry);
        this.paymentFailures   = Counter.builder("ecommerce.payment.failures.total")
                .description("Total payment API failures").register(registry);
        this.orderRequests     = Counter.builder("ecommerce.order.requests.total")
                .description("Total order API requests").register(registry);
        this.orderFailures     = Counter.builder("ecommerce.order.failures.total")
                .description("Total order API failures").register(registry);
        this.inventoryRequests = Counter.builder("ecommerce.inventory.requests.total")
                .description("Total inventory API requests").register(registry);
        this.inventoryFailures = Counter.builder("ecommerce.inventory.failures.total")
                .description("Total inventory API failures").register(registry);

        // Latency timers per service
        this.checkoutTimer  = Timer.builder("ecommerce.checkout.response.time")
                .description("Checkout API response time").register(registry);
        this.paymentTimer   = Timer.builder("ecommerce.payment.response.time")
                .description("Payment API response time").register(registry);
        this.orderTimer     = Timer.builder("ecommerce.order.response.time")
                .description("Order API response time").register(registry);
        this.inventoryTimer = Timer.builder("ecommerce.inventory.response.time")
                .description("Inventory API response time").register(registry);

        // Active connections gauge
        Gauge.builder("ecommerce.active.connections", activeConnections, AtomicInteger::get)
                .description("Currently active API connections").register(registry);
    }

    public void recordCheckoutRequest()  { checkoutRequests.increment(); }
    public void recordCheckoutFailure()  { checkoutFailures.increment(); }
    public void recordPaymentRequest()   { paymentRequests.increment(); }
    public void recordPaymentFailure()   { paymentFailures.increment(); }
    public void recordOrderRequest()     { orderRequests.increment(); }
    public void recordOrderFailure()     { orderFailures.increment(); }
    public void recordInventoryRequest() { inventoryRequests.increment(); }
    public void recordInventoryFailure() { inventoryFailures.increment(); }

    public Timer getCheckoutTimer()  { return checkoutTimer; }
    public Timer getPaymentTimer()   { return paymentTimer; }
    public Timer getOrderTimer()     { return orderTimer; }
    public Timer getInventoryTimer() { return inventoryTimer; }

    public void incrementActiveConnections() { activeConnections.incrementAndGet(); }
    public void decrementActiveConnections() { activeConnections.decrementAndGet(); }
}
