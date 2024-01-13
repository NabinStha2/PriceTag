package com.example.pricetag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
  public Category findByCategoryName(String name);
}
