package com.brain.model.service;

import java.util.Date;

public class ProductServiceModel {

    private String name;
    private String category;
    private String description;
    private int quantity;
    private Date createdDate;
    private Date lastModifiedDate;

    public ProductServiceModel() {
    }

    public String getName() {
        return name;
    }

    public ProductServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductServiceModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductServiceModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ProductServiceModel setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ProductServiceModel setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
}
