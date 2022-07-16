package com.brain.model.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity{

    @Expose
    @NotBlank(message = "The name cannot be blank!")
    @Column(name = "name")
    private String name;

    @Expose
    @NotBlank(message = "The category cannot be blank!")
    @Column(name = "category")
    private String category;

    @Expose
    @NotBlank(message = "The description cannot be blank!")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Expose
    @Min(value = 1, message = "The quantity should not be less than 1")
    @NotNull(message = "Quantity cannot be null")
    @Column(name = "quantity")
    private int quantity;

    @Expose
    @NotNull(message = "The created date cannot be null!")
    @Column(name = "created_date")
    private Date createdDate;

    @Expose
    @NotNull(message = "The last modified date date cannot be null!")
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    public ProductEntity() {
    }

    public String getName() {
        return name;
    }

    public ProductEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductEntity setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ProductEntity setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ProductEntity setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
}
