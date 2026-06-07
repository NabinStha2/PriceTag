package com.example.pricetag.features.media.service;

import com.example.pricetag.enums.EntityType;
import com.example.pricetag.features.media.dto.response.MediaResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface MediaService {

    MediaResponseDto saveSinglemedia(Long entityId, EntityType entityType, MultipartFile file,
                                     String entityName);

    void saveMultimedias(Long entityId, EntityType entityType, MultipartFile[] files,
                         String entityName);

    void replaceProductmedias(Long productId, EntityType entityType, MultipartFile[] files);

    void deletemedias(Long entityId, EntityType type);

    List<MediaResponseDto> getmedias(Long entityId, EntityType type);

    MediaResponseDto getMainMedia(Long entityId, EntityType type);

    Map<Long, MediaResponseDto> getPrimarymedias(List<Long> entityIds, EntityType type);
}