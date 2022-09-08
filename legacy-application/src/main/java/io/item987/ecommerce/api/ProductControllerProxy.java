package io.item987.ecommerce.api;

import static io.item987.ecommerce.api.Constants.API_URL_PRODUCTS;

import io.item987.ecommerce.product.ProductData;
import io.item987.ecommerce.product.ProductServiceClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(API_URL_PRODUCTS)
public class ProductControllerProxy {

    private final ProductServiceClient productServiceClient;

    public ProductControllerProxy(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @GetMapping
    public List<ProductData> getProducts(@RequestParam(name = "ids", required = false) List<Long> ids) {
        return productServiceClient.getProducts(ids != null ? ids : Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ProductData getProduct(@PathVariable long id) {
        return productServiceClient.getProduct(id);
    }

    @PostMapping
    public ProductData createProduct(@RequestBody ProductData productData) {
        return productServiceClient.createProduct(productData);
    }

    @PutMapping
    public ProductData updateProduct(@RequestBody ProductData productData) {
        return productServiceClient.updateProduct(productData);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productServiceClient.deleteProduct(id);
    }
}
