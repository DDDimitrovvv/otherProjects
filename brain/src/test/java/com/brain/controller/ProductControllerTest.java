package com.brain.controller;

import com.brain.model.entities.ProductEntity;
import com.brain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductControllerTest.class);
    private static final String PRODUCT_CONTROLLER_PREFIX = "/products";
    private long testProductId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp() {
        this.init();
    }

    @AfterEach
    public void setDown() {
        if (productRepository.getCountOfAllProductEntities() != 0) {
            productRepository.deleteAll();
        }
    }


    @Test
    void showSpecificProductByIdAndCheckForCategoriesCount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get(PRODUCT_CONTROLLER_PREFIX + "/{id}", testProductId)).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(2, productRepository.getCountOfAllProductEntitiesByCategory("Notebook"));
    }

    @Test
    void showProductsByPageAndOrdered() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get(PRODUCT_CONTROLLER_PREFIX + "?orderBy=category&direction=DESC&page=1&pageSize=10")).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(2, productRepository.getCountOfAllProductEntities());
    }

    @Test
    void showProductsOrderedWithoutPaging() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get(PRODUCT_CONTROLLER_PREFIX + "?orderBy=category&direction=DESC")).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(2, productRepository.getCountOfAllProductEntities());
    }


    @Test
    void storeNewProductInDatabase() throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("name", "Dell");
        body.put("category", "Notebook");
        body.put("description", "With simple 16:9 IPS screen");
        body.put("quantity", "4");
        body.put("createdDate", "2020-05-06");
        body.put("lastModifiedDate", "2021-05-06");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.
                post(PRODUCT_CONTROLLER_PREFIX + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(3, productRepository.getCountOfAllProductEntitiesByCategory("Notebook"));
    }

    @Test
    void storeNewProductInDatabaseNotSuccessfully() throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("name", "Dell");
        body.put("category", "");
        body.put("description", "With simple 16:9 IPS screen");
        body.put("quantity", "4");
        body.put("createdDate", "2020-05-06");
        body.put("lastModifiedDate", "2021-05-06");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.
                post(PRODUCT_CONTROLLER_PREFIX + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Assertions.assertEquals(2, productRepository.getCountOfAllProductEntitiesByCategory("Notebook"));
    }

    @Test
    void updateAlreadyExistingProductInDatabase() throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("name", "Dell");
        body.put("category", "Monitor");
        body.put("description", "Simple 16:9 screen");
        body.put("quantity", "4");
        body.put("createdDate", "2020-05-06");
        body.put("lastModifiedDate", "2021-05-06");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.
                put(PRODUCT_CONTROLLER_PREFIX + "/update/{id}", testProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(1, productRepository.getCountOfAllProductEntitiesByCategory("Notebook"));
    }


    @Test
    void deleteProductByGivenId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                delete(PRODUCT_CONTROLLER_PREFIX + "/delete/{id}", testProductId)).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(1, productRepository.getCountOfAllProductEntitiesByCategory("Notebook"));
    }

    @Test
    void deleteAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                delete(PRODUCT_CONTROLLER_PREFIX + "/delete/all", testProductId)).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(0, productRepository.getCountOfAllProductEntities());
    }

    @Test
    void decreaseQuantityOfProductByGivenIdSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                post(PRODUCT_CONTROLLER_PREFIX + "/{id}/order/{quantity}", testProductId, 1)).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(1, productRepository.getProductQuantityByReceivedId(testProductId));
    }

    @Test
    void decreaseQuantityOfProductByGivenIdNotSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                post(PRODUCT_CONTROLLER_PREFIX + "/{id}/order/{quantity}", testProductId, 10)).
                andExpect(status().is4xxClientError());

        Assertions.assertEquals(2, productRepository.getProductQuantityByReceivedId(testProductId));
    }

    @Test
    void decreaseQuantityOfProductByGivenIdNotSuccessfullyWithNegativeInputQuantity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get(PRODUCT_CONTROLLER_PREFIX + "/{id}/order/{quantity}", testProductId, -1)).
                andExpect(status().is4xxClientError());

        Assertions.assertEquals(2, productRepository.getProductQuantityByReceivedId(testProductId));
    }

    @Test
    void showCategoriesGroupWithQuantity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get(PRODUCT_CONTROLLER_PREFIX + "/categories")).
                andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(List.of("Notebook,6"), productRepository.groupedCategoriesAndSumTheQuantity());
    }

    private void init() {
        try {
            ProductEntity productEntity1 = new ProductEntity()
                    .setName("Dell Latitude")
                    .setCategory("Notebook")
                    .setDescription("Business laptop for every day.")
                    .setQuantity(2)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-06-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-08"));

            ProductEntity productEntity2 = new ProductEntity()
                    .setName("Acer Nitro")
                    .setCategory("Notebook")
                    .setDescription("Gaming laptop for every day.")
                    .setQuantity(4)
                    .setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-12"))
                    .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-07"));

            productRepository.saveAll(List.of(productEntity1, productEntity2));
            testProductId = productEntity1.getId();

        } catch (ParseException exception) {
            LOGGER.error("Error during Date object creation: " + exception.getMessage());
        }
    }
}
