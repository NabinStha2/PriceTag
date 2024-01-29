package com.example.pricetag.services;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.responses.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoryService {
    CommonResponseDto getAllSubCategories();

    CommonResponseDto editSubCategory(SubCategoryDto subCategoryDto);

}
