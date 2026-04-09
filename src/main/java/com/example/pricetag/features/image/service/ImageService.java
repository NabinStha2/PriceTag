package com.example.pricetag.features.image.service;

import com.example.pricetag.enums.ImageType;
import com.example.pricetag.features.image.dto.response.ImageResponseDto;
import com.example.pricetag.features.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ImageResponseDto saveSingleImage(Long entityId, ImageType entityType, MultipartFile file, String entityName);

    void saveMultiImages(Long entityId, ImageType entityType, MultipartFile[] files);

    void replaceProductImages(Long productId, ImageType entityType, MultipartFile[] files);

    void deleteImages(Long entityId, ImageType type);

    List<Image> getImages(Long entityId, ImageType type);

//    ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto);
}
