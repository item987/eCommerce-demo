package io.item987.ecommerce.product.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product not found")
class ProductNotFoundException extends RuntimeException {
}
