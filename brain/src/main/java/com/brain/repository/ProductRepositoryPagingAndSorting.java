package com.brain.repository;

import com.brain.model.entities.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryPagingAndSorting extends PagingAndSortingRepository<ProductEntity, Long> {

}
