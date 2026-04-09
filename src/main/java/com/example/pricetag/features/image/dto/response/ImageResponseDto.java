package com.example.pricetag.features.image.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ImageResponseDto {
    private Long id;

    private String url;

    private String publicId;

}
