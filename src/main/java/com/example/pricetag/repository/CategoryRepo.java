package com.example.pricetag.repository;

import com.example.pricetag.entity.Category;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Override
    @EntityGraph(attributePaths = {"subCategories"})
    Optional<Category> findById(@Nonnull Long id);

    Category findByCategoryName(String name);

    List<Category> findAllByIsActiveTrue();

    Boolean existsByCategoryName(String name);


}
