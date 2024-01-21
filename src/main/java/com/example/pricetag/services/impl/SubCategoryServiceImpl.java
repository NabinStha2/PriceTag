package com.example.pricetag.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

  @Autowired
  private SubCategoryRepo subCategoryRepo;

  @Override
  public CommonResponseDto getAllSubCategories() {
    List<SubCategory> subCategories = subCategoryRepo.findAll();

    List<SubCategoryDto> subCategoryDto = new ArrayList<>();
    subCategories.stream().forEach((sub) -> {
      SubCategoryDto newSubCategoryDto = new SubCategoryDto();
      newSubCategoryDto.setId(sub.getId());
      newSubCategoryDto.setSubCategoryName(sub.getSubCategoryName());

      CategoryDto newCategoryDto = new CategoryDto();
      newCategoryDto.setId(sub.getCategory().getId());
      newCategoryDto.setCategoryName(sub.getCategory().getCategoryName());

      newSubCategoryDto.setCategory(newCategoryDto);

      subCategoryDto.add(newSubCategoryDto);
    });

    return CommonResponseDto
        .builder()
        .message("Success")
        .data(Map.of("results", subCategoryDto))
        .success(true)
        .build();

  }

  @Override
  public SubCategory editSubCategory(SubCategoryDto subCategoryDto) throws ApplicationException {

    Optional<SubCategory> existingSubCategoryOptional = subCategoryRepo.findById(subCategoryDto.getId());
    if (existingSubCategoryOptional.isPresent()) {

      SubCategory existingSubCategory = existingSubCategoryOptional.get();

      existingSubCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());

      try {
        SubCategory savedSubCategory = subCategoryRepo.save(existingSubCategory);

        return savedSubCategory;
      } catch (DataAccessException ex) {
        throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      throw new ApplicationException("404", " Category not found", HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public CommonResponseDto getProductsWithSubCategoryId(SubCategoryDto subCategoryDto)
    throws ApplicationException {

    Optional<SubCategory> existingSubCategoryOptional = subCategoryRepo.findById(subCategoryDto.getId());
    if (existingSubCategoryOptional.isPresent()) {

      SubCategory existingSubCategory = existingSubCategoryOptional.get();

      return CommonResponseDto
          .builder()
          .message("Product fetch Successfully")
          .data(Map.of("results", existingSubCategory.getProduct()))
          .success(true)
          .build();
    } else {
      throw new ApplicationException("404", "Sub Category not found", HttpStatus.NOT_FOUND);
    }
  }

}
