package com.alexvait.ordergateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancedRoutesConfig {

    @Bean
    public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/orders*", "/api/v1/orders/*")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName("ordersCircuitBreaker")
                                        .setFallbackUri("forward:/orders-failover")
                                        .setRouteId("orders-failover")
                        ))
                        .uri("lb://order-api")
                        .id("order-api")
                )
                .route(r -> r.path("/api/v1/orders/order*", "/api/v1/orders/order/*")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName("ordersCircuitBreaker")
                                        .setFallbackUri("forward:/orders-failover")
                                        .setRouteId("orders-failover")
                        ))
                        .uri("lb://order-api")
                        .id("order-api")
                )
                .route(r -> r.path("/api/v1/orders/customer*", "/api/v1/orders/customer/*")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName("ordersCircuitBreaker")
                                        .setFallbackUri("forward:/orders-failover")
                                        .setRouteId("orders-failover")
                        ))
                        .uri("lb://order-api")
                        .id("order-api")
                )
                .route(r -> r.path("/orders-failover/**")
                        .uri("lb://ms-failover")
                        .id("orders-failover-service")
                )
                .build();
    }

}
