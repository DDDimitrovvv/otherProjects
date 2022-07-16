package com.brain.service;

import com.brain.model.entities.ProductEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<ProductEntity> listAllProducts();

    ResponseEntity<?> saveProduct(ProductEntity product);

    ResponseEntity<?> updateProduct(ProductEntity product, Long id);

    ResponseEntity<?> showProductById(Long id);

    ResponseEntity<?> deleteProduct(Long id);

    void seedProducts();
}
