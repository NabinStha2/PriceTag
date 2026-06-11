package com.example.pricetag.features.Variant.repository;


import com.example.pricetag.entity.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepo extends JpaRepository<Variants, Long> {
    boolean existsBySku(String sku);
}
