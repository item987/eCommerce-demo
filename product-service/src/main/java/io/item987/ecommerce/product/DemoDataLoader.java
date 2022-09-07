package io.item987.ecommerce.product;

import io.item987.ecommerce.product.model.Product;
import io.item987.ecommerce.product.model.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Component
class DemoDataLoader implements ApplicationRunner {

    private final ProductRepository productRepository;

    DemoDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createProductsCatalogDemo();
    }

    private void createProductsCatalogDemo() {
        var rnd = new Random();
        for (int i = 1; i <= 20; i++)
            productRepository.save(new Product("Sample Product " + (i < 10 ? "0" + i : i), rnd.nextInt(1, 1000)));
    }

}
