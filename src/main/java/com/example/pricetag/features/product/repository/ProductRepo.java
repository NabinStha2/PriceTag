package com.example.pricetag.features.product.repository;

import com.example.pricetag.features.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findAllByIsActiveTrue(Pageable pageable);

    List<Product> findAllBySubCategoryIdAndNameContainingIgnoreCase(Long subCategoryId, Pageable pageable, String name);

    Page<Product> findAllBySubCategoryIdAndIsActiveTrue(Long subCategoryId, Pageable pageable);
}
