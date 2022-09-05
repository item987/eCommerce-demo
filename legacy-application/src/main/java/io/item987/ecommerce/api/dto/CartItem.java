package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.order.OrderDetail;
import io.item987.ecommerce.order.OrderDetailModel;

public record CartItem(Long orderDetailId, ProductData product, int quantity) implements OrderDetailModel {

    public CartItem(OrderDetail orderDetail) {
        this(orderDetail.getId(),
             new ProductData(
                orderDetail.getProduct().getId(),
                orderDetail.getProduct().getName(),
                orderDetail.getProduct().getPrice()),
             orderDetail.getQuantity());
    }

    @Override
    public Long id() {
        return orderDetailId;
    }

    @Override
    public long productId() {
        return product.id();
    }
}
