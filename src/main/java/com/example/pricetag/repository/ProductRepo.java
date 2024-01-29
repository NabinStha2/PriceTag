package com.example.pricetag.repository;

import com.example.pricetag.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findAllBySubCategoryId(Long subCategoryId, Pageable pageable);

}
