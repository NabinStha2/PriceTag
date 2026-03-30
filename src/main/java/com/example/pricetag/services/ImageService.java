package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.ImageDto;
import org.springframework.http.ResponseEntity;

public interface ImageService {

    ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto);
}
