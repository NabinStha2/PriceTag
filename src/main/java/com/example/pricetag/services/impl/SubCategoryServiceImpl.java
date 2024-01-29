package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public CommonResponseDto getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepo.findAll();

        List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();
        subCategories.forEach((sub) -> {

            CategoryDto newCategoryDto = new CategoryDto();
            newCategoryDto.setId(sub.getCategory().getId());
            newCategoryDto.setCategoryName(sub.getCategory().getCategoryName());

            SubCategoryDto newSubCategoryDto = new SubCategoryDto();
            newSubCategoryDto.setId(sub.getId());
            newSubCategoryDto.setSubCategoryName(sub.getSubCategoryName());

            subCategoryDtoList.add(newSubCategoryDto);
        });

        return CommonResponseDto
                .builder()
                .message("Success")
                .data(Map.of("results", subCategoryDtoList))
                .success(true)
                .build();

    }

    @Override
    public CommonResponseDto editSubCategory(SubCategoryDto subCategoryDto) throws ApplicationException {
        Optional<SubCategory> existingSubCategoryOptional = subCategoryRepo.findById(subCategoryDto.getId());
        if (existingSubCategoryOptional.isPresent()) {
            SubCategory existingSubCategory = existingSubCategoryOptional.get();

            existingSubCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());

            try {
                SubCategory savedSubCategory = subCategoryRepo.save(existingSubCategory);

                return CommonResponseDto
                        .builder()
                        .message("SubCategory edited successfully")
                        .success(true)
                        .data(Map.of("results", savedSubCategory))
                        .build();

            } catch (DataAccessException ex) {
                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ApplicationException("404", " Category not found", HttpStatus.NOT_FOUND);
        }
    }


}
