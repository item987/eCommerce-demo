package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.order.OrderDetailList;

import java.util.List;

public record Cart(List<CartItem> items) implements OrderDetailList {
}
