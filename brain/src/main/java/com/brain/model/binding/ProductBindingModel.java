package com.brain.model.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

public class ProductBindingModel {

    @NotBlank(message = "The name cannot be blank!")
    private String name;

    @NotBlank(message = "The category cannot be blank!")
    private String category;

    @NotBlank(message = "The description cannot be blank!")
    private String description;

    @Min(value = 1, message = "The quantity should not be less than 1!")
    @NotNull(message = "Quantity cannot be null!")
    private int quantity;

    @NotNull(message = "The created date cannot be null!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "The created date cannot be in the future!")
    private Date createdDate;

    @NotNull(message = "The last modified date date cannot be null!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "The last modified date cannot be in the future!")
    private Date lastModifiedDate;

    public ProductBindingModel() {
    }

    public String getName() {
        return name;
    }

    public ProductBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductBindingModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductBindingModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ProductBindingModel setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ProductBindingModel setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
}
