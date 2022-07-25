package io.item987.ecommerce.api.dto;

import io.item987.ecommerce.product.Product;
import io.item987.ecommerce.product.ProductModel;

public record ProductData(Long id, String name, int price) implements ProductModel {

    public ProductData(Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }

}
