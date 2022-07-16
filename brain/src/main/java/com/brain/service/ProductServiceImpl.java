package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.repository.ProductRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final ResponseEntity<?> RESPONSE_ENTITY = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no product in the database with this ID!");
    private final ProductRepository productRepository;
    private final Gson gson;
    private final Resource productsFile;


    public ProductServiceImpl(@Value("classpath:input/products.json") Resource productsFile,
                              ProductRepository productRepository,
                              Gson gson) {
        this.productsFile = productsFile;
        this.productRepository = productRepository;
        this.gson = gson;
    }

    @Override
    public List<ProductEntity> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(ProductEntity product) {
        productRepository.save(product);
    }


    @Override
    public ResponseEntity<?> updateProduct(ProductEntity product, Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        product.setId(id);
        this.saveProduct(product);
        return new ResponseEntity<>("The product has been updated successfully!", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> showProductById(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        return new ResponseEntity<>(productRepository.findById(id).orElse(null), HttpStatus.OK);
//        try {
//            ProductEntity product = productServiceImpl.getProduct(id);
//            return new ResponseEntity<ProductEntity>(product, HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<ProductEntity>(HttpStatus.NOT_FOUND);
//        }

//        return new ResponseEntity<ProductEntity>(HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("The product has been deleted successfully!", HttpStatus.OK);
    }

    @Override
    public void seedProducts() {
        try {
            ProductEntity[] productEntities =
                    gson.fromJson(Files.
                            readString(Path.of(productsFile.getURI())), ProductEntity[].class);

            Arrays.stream(productEntities).
                    forEach(productRepository::save);
        } catch (IOException e) {
            throw new IllegalStateException("Sorry! The products cannot be imported into the DB!!!");
        }
    }

}
