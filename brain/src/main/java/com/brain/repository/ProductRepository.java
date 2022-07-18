package com.brain.repository;

import com.brain.model.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT COUNT(p) FROM ProductEntity AS p WHERE p.category=?1")
    int getCountOfAllProductEntitiesByCategory(String category);

    @Query("SELECT COUNT(p) FROM ProductEntity AS p")
    int getCountOfAllProductEntities();

    @Query("SELECT p.quantity FROM ProductEntity AS p WHERE p.id=?1")
    int getProductQuantityByReceivedId(Long id);

    @Query("SELECT p.category AS category, SUM(p.quantity) AS qunt from ProductEntity AS p GROUP BY p.category ORDER BY category ASC")
    List<String> groupedCategoriesAndSumTheQuantity();
}
