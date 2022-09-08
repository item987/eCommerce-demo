package io.item987.ecommerce.product.api;

import io.item987.ecommerce.product.model.Product;
import io.item987.ecommerce.product.model.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(name = "ids", required = false) List<Long> ids) {
        return ids == null || ids.isEmpty()
                ? productRepository.findAll(Sort.by("name"))
                : productRepository.findByIdInOrderByName(ids);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product productData) {
        return productRepository.save(new Product(productData.getName(), productData.getPrice()));
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product productData) {
        var product = getProduct(productData.getId());
        product.setName(productData.getName());
        product.setPrice(productData.getPrice());
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable long id) {
        productRepository.deleteById(id);
    }

}
