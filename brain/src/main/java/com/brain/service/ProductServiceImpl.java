package com.brain.service;

import com.brain.model.entities.ProductEntity;
import com.brain.model.service.ProductServiceModel;
import com.brain.repository.ProductRepository;
import com.brain.repository.ProductRepositoryPagingAndSorting;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final ResponseEntity<?> RESPONSE_ENTITY = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no product in the database with this ID!");
    private final ProductRepository productRepository;
    private final ProductRepositoryPagingAndSorting productRepositoryPagingAndSorting;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Resource productsFile;


    public ProductServiceImpl(@Value("classpath:input/products.json") Resource productsFile,
                              ProductRepository productRepository,
                              ProductRepositoryPagingAndSorting productRepositoryPagingAndSorting,
                              ModelMapper modelMapper,
                              Gson gson) {
        this.productsFile = productsFile;
        this.productRepository = productRepository;
        this.productRepositoryPagingAndSorting = productRepositoryPagingAndSorting;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    /**
     * This method is checking the stored in the DB items.
     * If there any it will return all stored items, otherwise error message with the corresponding HTTP status.
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> listAllProducts() {
        if (productRepository.getCountOfAllProductEntities() == 0) {
            return new ResponseEntity<>("There is no products stored in the database.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    /**
     * This method is mapping the input ProductServiceModel object to ProductEntity. Afterward, store it in the DB.
     * @param productServiceModel: input object received from POST request contains valid data of product entity
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> addProduct(ProductServiceModel productServiceModel) {
        ProductEntity productEntity = modelMapper.map(productServiceModel, ProductEntity.class);
        productRepository.save(productEntity);
        return new ResponseEntity<>("The product has been successfully added!", HttpStatus.OK);
    }


    /**
     * This method will check is there any item regarding the received id. If there is an item it will update the item's fields.
     * @param productServiceModel: input object received from POST request contains valid data of product entity
     * @param id: input id of stored product
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
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

    /**
     * This method will check for stored product item regarding the received id. If there is a match the product info will be returned.
     * @param id: input id of stored product
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> showProductById(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        return new ResponseEntity<>(productRepository.findById(id).get(), HttpStatus.OK);
    }

    /**
     * This method will check for stored product item regarding the received id. If there is a match this item will be deleted from the DB.
     * @param id: input id of stored product
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return RESPONSE_ENTITY;
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("The product has been successfully deleted!", HttpStatus.OK);
    }

    /**
     * This method will check the available stored items with categories.
     * If there are any it will return a JSON file with category and available products for each item.
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> showGroupedCategories() {
        List<String> categories = productRepository.groupedCategoriesAndSumTheQuantity();
        if (categories.isEmpty()) {
            return new ResponseEntity<>("There are no categories to be shown.", HttpStatus.BAD_REQUEST);
        }
        JSONArray jsonArray = new JSONArray();
        for ( String element : categories ){
            String category = element.trim().split(",")[ 0 ];
            Integer quantity = Integer.parseInt(element.trim().split(",")[ 1 ]);
            Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();
            sortedMap.put("category", category);
            sortedMap.put("productsAvailable", quantity);

            JSONObject jsonObject = new JSONObject(sortedMap);
            jsonArray.add(jsonObject);
        }

        return new ResponseEntity<>(jsonArray.toJSONString(), HttpStatus.OK);
    }

    /**
     * This method will validate the quantity of certain product.
     * It can decrease the quantity regarding the input quantity parameter.
     * @param id: input id of stored product
     * @param quantity: input number of product quantity
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> decreaseQuantity(Long id, int quantity) {

        if (quantity < 1) {
            return new ResponseEntity<>("The quantity should be a valid positive digit!", HttpStatus.BAD_REQUEST);
        }

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

    /**
     * This function will remove all stored products items in the DB.
     * @return ResponseEntity<?> with corresponding message and HTTP status
     */
    @Override
    public ResponseEntity<?> deleteAllProducts() {
        if (productRepository.getCountOfAllProductEntities() == 0) {
            return new ResponseEntity<>("There is no products stored in the database.", HttpStatus.BAD_REQUEST);
        }
        productRepository.deleteAll();
        return new ResponseEntity<>("All products have been successfully deleted.", HttpStatus.OK);
    }

    /**
     * This function will check for available products in the DB.
     * Afterwards, it will query the repository regarding the specific user request.
     * @param orderByElement: the name of the category for product (e.g. "name", "quantity", etc.)
     * @param directionOrder: the sorting order of the request - desc or asc
     * @param numberOfPages: the preferred page with products
     * @param countOfProducts: the count of visualized products
     * @return ResponseEntity<?> with corresponding message or JSON file with all products according to the clients request and HTTP status
     */
    @Override
    public ResponseEntity<?> showListWithProductsByPage(String orderByElement, String directionOrder, Integer numberOfPages, Integer countOfProducts) {
        if (productRepository.getCountOfAllProductEntities() == 0) {
            return new ResponseEntity<>("There is no products stored in the database.", HttpStatus.BAD_REQUEST);
        }

        List<ProductEntity> sortedListOfProducts = new ArrayList<>();
        if (orderByElement.isEmpty()) {
            return new ResponseEntity<>("There is no sorting category in your query.", HttpStatus.BAD_REQUEST);
        } else if ((numberOfPages == null || numberOfPages < 1) || (countOfProducts == null || countOfProducts < 1)) {
            Iterable<ProductEntity> allProductsSortedByCategory= null;
            if (directionOrder.equalsIgnoreCase("desc")) {
                allProductsSortedByCategory = productRepositoryPagingAndSorting.findAll(Sort.by(orderByElement).ascending());

            } else {
                allProductsSortedByCategory = productRepositoryPagingAndSorting.findAll(Sort.by(orderByElement).ascending());
            }
            allProductsSortedByCategory.iterator().forEachRemaining(sortedListOfProducts::add);
        } else {
            PageRequest pageRequest = null;
            if (directionOrder.equalsIgnoreCase("desc")) {
                pageRequest = PageRequest.of(numberOfPages - 1, countOfProducts, Sort.by(orderByElement).descending());
            } else {
                pageRequest = PageRequest.of(numberOfPages - 1, countOfProducts, Sort.by(orderByElement).ascending());
            }
            Page<ProductEntity> allPagesOfProducts = productRepositoryPagingAndSorting.findAll(pageRequest);
            sortedListOfProducts = allPagesOfProducts.getContent();

        }

        JSONArray jsonArray = new JSONArray();
        for ( ProductEntity element : sortedListOfProducts ){
            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> sortedMap = objectMapper.convertValue(element, LinkedHashMap.class);

            Date createdDate = new Date((long) sortedMap.get("createdDate"));
            Date lastModifiedDate = new Date((long) sortedMap.get("lastModifiedDate"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            sortedMap.put("createdDate", dateFormat.format(createdDate));
            sortedMap.put("lastModifiedDate", dateFormat.format(lastModifiedDate));
            String outputJsonObjectString = new Gson().toJson(sortedMap);
            jsonArray.add(outputJsonObjectString);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("products", jsonArray);
        jsonObject.put("totalRecords", productRepository.getCountOfAllProductEntities());

        return new ResponseEntity<>(jsonObject.toJSONString(), HttpStatus.OK);
    }

    /**
     * This method is reading already stored JSON file with preset values for products.
     * It will read them and afterwards store them in the DB.
     * This function will be executed every time the program is started.
     */
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
