package com.example.pricetag.controllers;

import com.example.pricetag.dto.ImageDto;
import com.example.pricetag.repository.ImageRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<CommonResponseDto> upload(ImageDto imageDto) {
        return imageService.uploadImage(imageDto);
    }
}
