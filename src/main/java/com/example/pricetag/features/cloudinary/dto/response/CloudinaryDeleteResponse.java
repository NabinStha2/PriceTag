package com.example.pricetag.features.cloudinary.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudinaryDeleteResponse {
    private String result; // "ok", "not found"
}