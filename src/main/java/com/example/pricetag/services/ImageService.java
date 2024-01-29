package com.example.pricetag.services;

import org.springframework.http.ResponseEntity;

import com.example.pricetag.dto.ImageDto;
import com.example.pricetag.responses.CommonResponseDto;

public interface ImageService {

  ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto);
}
