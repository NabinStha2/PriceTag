package com.example.pricetag.features.media.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaResponseDto {
    private Long attachmentId;
    private Long mediaAssetId;
    private String url;
    private String publicId;
    private String metadata;
    private String altText;
    private String role;
    private Integer displayOrder;
}
