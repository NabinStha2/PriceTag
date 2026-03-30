package com.example.pricetag.mapper;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.dto.category.CategoryDetailsDto;
import com.example.pricetag.dto.category.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    // single object mapping
    @Mapping(source = "categoryName",
            target = "name"
    )
    CategoryDto mapCategoryToCategoryDto(Category category);

    // list mapping (MapStruct automatically loops)
    List<CategoryDto> mapCategoryListToCategoryDtoList(List<Category> categories);

    @InheritInverseConfiguration
    Category mapCategoryDtoToCategory(CategoryDto categoryDto);


    @Mapping(source = "categoryName",
            target = "name")
    @Mapping(source = "subCategories",
            target = "subCategoryDtoList")
    CategoryDetailsDto mapCategoryToCategoryDetailsDto(Category category);

    @Mapping(source = "category.id",
            target = "categoryId")
    SubCategoryDto mapSubCategoryToSubCategoryDto(SubCategory subCategory);

    default LocalDateTime map(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
