package io.item987.ecommerce.product;

public class ProductServiceUnavailableException extends RuntimeException {

    public ProductServiceUnavailableException() {
        super("Error accessing products catalog");
    }

}
