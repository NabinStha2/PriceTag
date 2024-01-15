package com.example.pricetag.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.Product;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepo productRepo;
  @Autowired
  private CategoryRepo categoryRepo;
  @Autowired
  private SubCategoryRepo subCategoryRepo;

  @Override
  public CommonResponseDto createProduct(ProductDto productDto) {
    try {

      Optional<Category> existingCategory = categoryRepo.findById(productDto.getCategoryId());
      Optional<SubCategory> existingSubCategory = subCategoryRepo.findById(productDto.getSubCategoryId());

      if (existingCategory.isPresent() && existingSubCategory.isPresent()) {
        Category category = existingCategory.get();
        SubCategory subCategory = existingSubCategory.get();

        // Optional<SubCategory> filteredSubCategory =
        // category.getSubCategories().stream().filter(data -> {
        // return data.getId().equals(subCategory.getId());
        // }).findFirst();
        // if (filteredSubCategory.isPresent()) {
        // ColorLogger.logInfo(filteredSubCategory.get().toString());
        // } else {
        // ColorLogger.logError("No even number found in the list.");
        // }

        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setSubCategory(subCategory);
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setActualPrice(productDto.getActualPrice());
        newProduct.setDiscountedPrice(productDto.getDiscountedPrice());

        productRepo.save(newProduct);

        return CommonResponseDto
            .builder()
            .message("Product has been created successfully")
            .statusCode("201")
            .build();

      } else {
        throw new ApplicationException("404", "Category or SubCategory not found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      throw new ApplicationException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

}
