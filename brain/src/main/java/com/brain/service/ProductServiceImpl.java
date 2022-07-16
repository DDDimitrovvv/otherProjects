package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.model.service.ProductServiceModel;
import com.brain.repository.ProductRepository;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Resource productsFile;


    public ProductServiceImpl(@Value("classpath:input/products.json") Resource productsFile,
                              ProductRepository productRepository,
                              ModelMapper modelMapper,
                              Gson gson) {
        this.productsFile = productsFile;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public List<ProductEntity> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ResponseEntity<?> addProduct(ProductServiceModel productServiceModel) {
        ProductEntity productEntity = modelMapper.map(productServiceModel, ProductEntity.class);
        productRepository.save(productEntity);
        return new ResponseEntity<>("The product has been successfully added!", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> updateProduct(ProductServiceModel productServiceModel, Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        ProductEntity productEntity = modelMapper.map(productServiceModel, ProductEntity.class);
        productEntity.setId(id);
        productRepository.save(productEntity);
        return new ResponseEntity<>("The product has been  successfully updated!", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> showProductById(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        return new ResponseEntity<>(productRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("The product has been successfully deleted!", HttpStatus.OK);
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

    @Override
    public ResponseEntity<?> decreaseQuantity(Long id, int quantity) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        int availableProductQuantity = productRepository.getProductQuantityByReceivedId(id);
        ProductEntity productEntity = productRepository.findById(id).get();

        if (availableProductQuantity > quantity) {
            productEntity.setQuantity(availableProductQuantity - quantity);
            productRepository.save(productEntity);
            String outputString = String.format("You successfully bought products! Current quantity is %s of product with ID: %s.", (availableProductQuantity - quantity), id);

            return new ResponseEntity<>(outputString, HttpStatus.OK);

        } else if (availableProductQuantity == quantity) {
            productRepository.deleteById(id);
            String outputString = String.format("No quantity left for product with id: %s. This product is already sold!", id);
            return new ResponseEntity<>(outputString, HttpStatus.OK);

        } else {
            String outputString = String.format("There is not enough quantity of product with id: %s! Current quantity: %s.", id, availableProductQuantity);
            return new ResponseEntity<>(outputString, HttpStatus.BAD_REQUEST);
        }
    }

}
