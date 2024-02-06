package com.example.pricetag.repository;

import com.example.pricetag.entity.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepo extends JpaRepository<Variants, Long> {


}
