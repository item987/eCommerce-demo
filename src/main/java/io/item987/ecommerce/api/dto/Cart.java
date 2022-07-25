package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.order.OrderDetailList;
import io.item987.ecommerce.order.OrderDetailModel;

import java.util.List;

public record Cart(List<CartItem> items) implements OrderDetailList {
}
