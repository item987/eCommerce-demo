package io.item987.ecommerce.api;

import static io.item987.ecommerce.api.Constants.API_URL_PRODUCTS;

import io.item987.ecommerce.api.dto.ProductData;
import io.item987.ecommerce.product.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(API_URL_PRODUCTS)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductData> getAllProducts() {
        return productService.getAllProducts().stream().map(ProductData::new).toList();
    }

    @GetMapping("/{id}")
    public ProductData getProduct(@PathVariable long id) {
        return new ProductData(productService.getProduct(id));
    }

    @PostMapping
    public ProductData createProduct(@RequestBody ProductData productData) {
        return new ProductData(productService.createProduct(productData));
    }

    @PutMapping
    public ProductData updateProduct(@RequestBody ProductData productData) {
        return new ProductData(productService.updateProduct(productData));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
    }

}
