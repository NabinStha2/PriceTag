package com.example.pricetag.features.image.controller;

import com.example.pricetag.features.image.repository.ImageRepo;
import com.example.pricetag.features.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private ImageService imageService;

//    @PostMapping("/upload")
//    public ResponseEntity<CommonResponseDto> upload(ImageDto imageDto) {
//        return imageService.uploadImage(imageDto);
//    }


}
