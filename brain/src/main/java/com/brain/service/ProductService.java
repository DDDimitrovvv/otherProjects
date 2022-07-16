package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.model.service.ProductServiceModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<ProductEntity> listAllProducts();

    ResponseEntity<?> addProduct(ProductServiceModel productServiceModel);

    ResponseEntity<?> updateProduct(ProductServiceModel productServiceModel, Long id);

    ResponseEntity<?> showProductById(Long id);

    ResponseEntity<?> deleteProduct(Long id);

    void seedProducts();

    ResponseEntity<?> decreaseQuantity(Long id, int quantity);
}
