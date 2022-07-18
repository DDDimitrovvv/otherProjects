package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.model.service.ProductServiceModel;
import com.brain.repository.ProductRepository;
import com.brain.repository.ProductRepositoryPagingAndSorting;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceTest.class);

    private ProductServiceImpl productServiceTest;

    private ProductEntity productEntity1, productEntity2, productEntity3, productEntity4;

    @Autowired
    private ProductRepository mockProductRepository;

    @Autowired
    private ProductRepositoryPagingAndSorting productRepositoryPagingAndSorting;

    @Autowired
    ModelMapper mockModelMapper;

    @Mock
    Resource productsFile;

    @Autowired
    Gson gson;

    @BeforeEach
    public void setUp() {
        this.init();
        mockProductRepository = Mockito.mock(ProductRepository.class);
        productServiceTest = new ProductServiceImpl(productsFile, mockProductRepository, productRepositoryPagingAndSorting, mockModelMapper, gson);
    }


    @Test
    public void getAllAvailableProducts() {
        when(mockProductRepository.findAll()).thenReturn(List.of(productEntity1, productEntity2));
        List<ProductEntity> productEntities2 = productServiceTest.listAllProducts();
        Assertions.assertEquals(2, productEntities2.size());
    }

    @Test
    public void updateProductByGivenId() {
        try {
            productEntity4 = new ProductEntity()
                    .setName("Asus N Series")
                    .setCategory("Notebook")
                    .setDescription("Man's laptop for every day.")
                    .setQuantity(5)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-03-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-07"));
        } catch (ParseException exception) {
            LOGGER.error("Error during Date object creation: " + exception.getMessage());

            ProductServiceModel productServiceModel = mockModelMapper.map(productEntity4, ProductServiceModel.class);

            productServiceTest.updateProduct(productServiceModel, 1L);
            Mockito.verify(mockProductRepository).save(productEntity4);
        }
    }


    @Test
    public void deleteProductByGivenId() {
        mockProductRepository.deleteById(productEntity2.getId());
        Mockito.verify(mockProductRepository).deleteById(productEntity2.getId());
        Assertions.assertEquals(0,mockProductRepository.count());
    }


    @Test
    public void storeProductInDatabase() {
        try {
            productEntity3 = new ProductEntity()
                    .setName("HP Probook")
                    .setCategory("Notebook")
                    .setDescription("Man's laptop for every day.")
                    .setQuantity(1)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-07-07"));
        } catch (ParseException exception) {
            LOGGER.error("Error during Date object creation: " + exception.getMessage());
        }
        mockProductRepository.save(productEntity3);

        Mockito.verify(mockProductRepository).save(productEntity3);

    }


    private void init() {
        try {
            productEntity1 = new ProductEntity()
                    .setName("Dell Latitude")
                    .setCategory("Notebook")
                    .setDescription("Business laptop for every day.")
                    .setQuantity(2)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-06-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-08"));

            productEntity2 = new ProductEntity()
                    .setName("Acer Nitro")
                    .setCategory("Notebook")
                    .setDescription("Gaming laptop for every day.")
                    .setQuantity(4)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-07"));


        } catch (ParseException exception) {
            LOGGER.error("Error during Date object creation: " + exception.getMessage());
        }
    }
}
