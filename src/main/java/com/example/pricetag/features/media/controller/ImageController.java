package com.example.pricetag.features.media.controller;

import com.example.pricetag.features.media.repository.MediaAssetRepo;
import com.example.pricetag.features.media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    @Autowired
    private MediaAssetRepo mediaAssetRepo;

    @Autowired
    private MediaService mediaService;

//    @PostMapping("/upload")
//    public ResponseEntity<CommonResponseDto> upload(ImageDto imageDto) {
//        return imageService.uploadImage(imageDto);
//    }


}
