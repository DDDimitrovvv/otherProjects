package com.brain.controller;

import com.brain.model.binding.ProductBindingModel;
import com.brain.model.entities.ProductEntity;
import com.brain.model.service.ProductServiceModel;
import com.brain.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public List<ProductEntity> listAllProducts() {
        return productService.listAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByID(@PathVariable Long id) {
        return productService.showProductById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductInDB(@RequestBody @Valid ProductBindingModel productAddBindingModel,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes) {
        ResponseEntity<?> errorMessagesContainer = validateInputProductObject(productAddBindingModel, bindingResult, redirectAttributes);
        if (errorMessagesContainer != null) return errorMessagesContainer;
        return productService.addProduct(modelMapper.map(productAddBindingModel, ProductServiceModel.class));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExistingProductByID(@RequestBody @Valid ProductBindingModel productUpdateBindingModel,
                                                       @PathVariable Long id,
                                                       BindingResult bindingResult,
                                                       RedirectAttributes redirectAttributes) {
        ResponseEntity<?> errorMessagesContainer = validateInputProductObject(productUpdateBindingModel, bindingResult, redirectAttributes);
        if (errorMessagesContainer != null) return errorMessagesContainer;
        return productService.updateProduct(modelMapper.map(productUpdateBindingModel, ProductServiceModel.class), id);
    }

    @PostMapping("/{id}/order/{quantity}")
    public ResponseEntity<?> decreaseProductQuantity(@PathVariable Long id, @PathVariable int quantity){
        return productService.decreaseQuantity(id, quantity);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> visualizeCategoriesWithTotalQuantity(){
        return productService.showGroupedCategories();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductByGivenID(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    private ResponseEntity<?> validateInputProductObject(@RequestBody @Valid ProductBindingModel productBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.productAddBindingModel",
                    bindingResult);
            StringBuilder errorMessagesContainer = new StringBuilder();
            bindingResult.getFieldErrors().forEach(fieldError -> errorMessagesContainer.append(fieldError.getDefaultMessage()).append(System.lineSeparator()));
            return new ResponseEntity<>(errorMessagesContainer, HttpStatus.BAD_REQUEST);

        }
        return null;
    }


}
