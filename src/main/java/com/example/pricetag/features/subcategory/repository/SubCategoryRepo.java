package com.example.pricetag.features.subcategory.repository;

import com.example.pricetag.features.subcategory.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    SubCategory findBySubCategoryName(String name);

    boolean existsByCategoryIdAndSubCategoryNameIgnoreCaseAndIsDeletedFalse(Long subCategoryId,
                                                                            String subCategoryName);
}
