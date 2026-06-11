package com.example.pricetag.features.Variant.repository;

import com.example.pricetag.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepo extends JpaRepository<Size, Long> {
}

