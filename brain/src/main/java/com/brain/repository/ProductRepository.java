package com.brain.repository;

import com.brain.model.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT COUNT(p) FROM ProductEntity AS p WHERE p.category=?1")
    int getCountOfAllProductEntitiesByCategory(String category);

    @Query("SELECT COUNT(p) FROM ProductEntity AS p")
    int getCountOfAllProductEntities();
}
