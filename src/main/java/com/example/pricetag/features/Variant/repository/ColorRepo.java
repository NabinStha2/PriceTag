package com.example.pricetag.features.Variant.repository;

import com.example.pricetag.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepo extends JpaRepository<Color, Long> {
}

