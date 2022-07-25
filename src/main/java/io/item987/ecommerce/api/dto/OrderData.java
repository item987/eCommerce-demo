package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.order.Order;
import io.item987.ecommerce.order.OrderStatus;

import java.time.Instant;

public record OrderData(Long id, Instant creationDate, OrderStatus status) {

    public OrderData(Order order) {
        this(order.getId(), order.getCreationDate(), order.getStatus());
    }

}
