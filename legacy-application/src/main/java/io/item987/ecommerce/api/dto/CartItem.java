package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.order.OrderDetail;
import io.item987.ecommerce.order.OrderDetailModel;
import io.item987.ecommerce.product.ProductData;

public record CartItem(Long orderDetailId, ProductData product, int quantity) implements OrderDetailModel {

    @Override
    public Long id() {
        return orderDetailId;
    }

    @Override
    public long productId() {
        return product.id();
    }
}
