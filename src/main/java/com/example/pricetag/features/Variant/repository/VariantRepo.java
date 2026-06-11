package com.example.pricetag.features.Variant.repository;


import com.example.pricetag.entity.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepo extends JpaRepository<Variants, Long> {
    boolean existsBySku(String sku);

    List<Variants> findAllByIsActiveTrue();

    Variants findByIdAndIsActiveTrue(Long id);

    List<Variants> findAllByProductIdAndIsActiveTrue(Long productId);
}
