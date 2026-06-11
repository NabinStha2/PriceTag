package com.example.pricetag.features.Variant.mapper;


import com.example.pricetag.entity.Variants;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VariantMapper {
    Variants mapVariantRequestDtoToVariant(VariantRequestDto variantRequestDto);

    VariantResponseDto mapVariantToVariantResponseDto(Variants variant);

    List<VariantResponseDto> mapVariantListToVariantResponseDtoList(
            List<Variants> variants);
}
