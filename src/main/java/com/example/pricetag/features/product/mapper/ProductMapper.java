package com.example.pricetag.features.product.mapper;

import com.example.pricetag.features.category.mapper.CategoryMapper;
import com.example.pricetag.features.image.dto.response.ImageResponseDto;
import com.example.pricetag.features.image.entity.Image;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import com.example.pricetag.features.product.dto.response.SingleProductDetailsResponseDto;
import com.example.pricetag.features.product.entity.Product;
import com.example.pricetag.features.subcategory.mapper.SubCategoryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, SubCategoryMapper.class})
public interface ProductMapper {
    ProductResponseDto mapProductToProductResponseDto(Product product);

    List<ProductResponseDto> mapProductListToProductResponseDtoList(List<Product> products);

    @Mapping(target = "imageUrl",
            ignore = true)
    SingleProductDetailsResponseDto mapProductToSingleProductDetailsResponseDto(Product product);

    List<ImageResponseDto> mapImagesToImageResponseDtoList(List<Image> images);

    @Mapping(source = "imageUrl",
            target = "url")
    @Mapping(source = "imagePublicId",
            target = "publicId")
    ImageResponseDto mapImageToImageResponseDto(Image image);

}
