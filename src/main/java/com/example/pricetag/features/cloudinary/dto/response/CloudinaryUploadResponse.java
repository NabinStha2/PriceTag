package com.example.pricetag.features.cloudinary.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudinaryUploadResponse {
    private String url;
    private String publicId;
}
