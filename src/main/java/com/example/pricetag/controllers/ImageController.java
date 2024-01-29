package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.ImageDto;
import com.example.pricetag.repository.ImageRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ImageService;

@RestController
public class ImageController {

  @Autowired
  private ImageRepo imageRepo;

  @Autowired
  private ImageService imageService;

  @PostMapping("/upload")
  public ResponseEntity<CommonResponseDto> upload(ImageDto imageDto) {
    try {
      return imageService.uploadImage(imageDto);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
