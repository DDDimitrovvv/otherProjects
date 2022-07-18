package com.brain.component;

import com.brain.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements CommandLineRunner {

    private final ProductService productService;

    public ApplicationInit(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
//        Another option for insertion values into the table "products" by using a JSON file - instead of liquibase functionality
//        productService.seedProducts();
    }
}
