package com.example.pricetag.features.subcategory.mapper;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.subcategory.dto.request.CreateSubCategoryRequestDto;
import com.example.pricetag.features.subcategory.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubCategoryMapper {
    SubCategoryDto mapSubCategoryToSubCategoryDto(SubCategory subCategory);

    List<SubCategoryDto> mapSubCategoryListToSubCategoryDtoList(List<SubCategory> subCategories);

    SubCategory mapCreateSubCategoryRequestDtoToSubCategory(CreateSubCategoryRequestDto createSubCategoryRequestDto);
}
