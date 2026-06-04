package com.example.pricetag.features.media.service;

import com.example.pricetag.enums.EntityType;
import com.example.pricetag.features.media.dto.response.ImageResponseDto;
import com.example.pricetag.features.media.entity.MediaAsset;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ImageService {

    ImageResponseDto saveSingleImage(Long entityId, EntityType entityType, MultipartFile file,
                                     String entityName);

    void saveMultiImages(Long entityId, EntityType entityType, MultipartFile[] files);

    void replaceProductImages(Long productId, EntityType entityType, MultipartFile[] files);

    void deleteImages(Long entityId, EntityType type);

    List<MediaAsset> getImages(Long entityId, EntityType type);

}
