package io.item987.ecommerce.product;

import io.item987.ecommerce.order.OrderDetailRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by("name"));
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found"));
    }

    public Product createProduct(String name, int price) {
        return productRepository.save(new Product(name, price));
    }

    public Product createProduct(ProductModel productModel) {
        return createProduct(productModel.name(), productModel.price());
    }

    public Product updateProduct(ProductModel productModel) {
        var product = getProduct(productModel.id());
        product.setName(productModel.name());
        product.setPrice(productModel.price());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(long id) {
        orderDetailRepository.deleteByProduct(id);
        productRepository.deleteById(id);
    }

}
