package io.item987.ecommerce;

import io.item987.ecommerce.order.OrderService;
import io.item987.ecommerce.product.ProductService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Component
class DemoDataLoader implements ApplicationRunner {

    private final ProductService productService;

    DemoDataLoader(ProductService productService, OrderService orderService) {
        this.productService = productService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createProductsCatalogDemo();
    }

    private void createProductsCatalogDemo() {
        var rnd = new Random();
        for (int i = 1; i <= 20; i++)
            productService.createProduct("Sample Product " + (i < 10 ? "0" + i : i), rnd.nextInt(1, 1000));
    }

}
