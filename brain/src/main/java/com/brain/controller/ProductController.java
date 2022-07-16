package com.brain.controller;

import com.brain.model.entities.ProductEntity;
import com.brain.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping("/all")
    public List<ProductEntity> list() {
        return productServiceImpl.listAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return productServiceImpl.showProductById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ProductEntity product) {
        return productServiceImpl.saveProduct(product);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody ProductEntity product, @PathVariable Long id) {
        return productServiceImpl.updateProduct(product, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return productServiceImpl.deleteProduct(id);
    }
}
