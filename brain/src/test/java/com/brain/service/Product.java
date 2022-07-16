package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class Product {

    private ProductServiceImpl productServiceTest;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        this.init();
//        productServiceTest = new ProductServiceImpl();

    }



    @Test
    public void getAllProducts(){
//        List<ProductEntity> productEntities =
    }

    @Test
    public void updateProduct(){

    }


    private void init() {
    }
}
