package com.example.pricetag.features.product.mapper;

import com.example.pricetag.features.category.mapper.CategoryMapper;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import com.example.pricetag.features.product.entity.Product;
import com.example.pricetag.features.subcategory.mapper.SubCategoryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, SubCategoryMapper.class})
public interface ProductMapper {
    ProductResponseDto mapProductToProductResponseDto(Product product);

    List<ProductResponseDto> mapProductListToProductResponseDtoList(List<Product> products);

}