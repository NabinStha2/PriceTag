package com.example.pricetag.features.media.service.impl;

import com.example.pricetag.enums.EntityType;
import com.example.pricetag.enums.MediaRole;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.cloudinary.dto.response.CloudinaryUploadResponse;
import com.example.pricetag.features.cloudinary.service.CloudinaryService;
import com.example.pricetag.features.media.dto.response.MediaResponseDto;
import com.example.pricetag.features.media.entity.MediaAsset;
import com.example.pricetag.features.media.entity.MediaAttachment;
import com.example.pricetag.features.media.mapper.MediaMapper;
import com.example.pricetag.features.media.repository.MediaAssetRepo;
import com.example.pricetag.features.media.repository.MediaAttachmentRepo;
import com.example.pricetag.features.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png",
                                                                 "image/webp", "image/gif");

    private final CloudinaryService cloudinaryService;
    private final MediaAssetRepo mediaAssetRepository;
    private final MediaAttachmentRepo mediaAttachmentRepository;
    private final MediaMapper mediaMapper;

    private static String computeSha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to compute hash", ex);
        }
    }

    @Override
    @Transactional
    public MediaResponseDto saveSinglemedia(Long entityId, EntityType entityType,
                                            MultipartFile file, String entityName) {
        validateFile(file);
        MediaAsset asset = findOrUploadAsset(file, entityType, entityName);
        // determine display order (0 if none)
        long displayOrder = mediaAttachmentRepository.countByEntityTypeAndEntityIdAndIsActiveTrue(
                entityType, entityId);
        MediaRole role = displayOrder == 0 ? MediaRole.MAIN : MediaRole.GALLERY;
        MediaAttachment attachment = MediaAttachment
                .builder()
                .mediaAsset(asset)
                .entityType(entityType)
                .entityId(entityId)
                .role(role)
                .displayOrder((int) displayOrder)
                .isActive(true)
                .isDeleted(false)
                .build();
        mediaAttachmentRepository.save(attachment);

        // increment reference count
        incrementReferenceCount(asset);

        return mediaMapper.mapMediaAttachmentToMediaResponseDto(attachment);
    }

    @Override
    @Transactional
    public void saveMultimedias(Long entityId, EntityType entityType, MultipartFile[] files,
                                String entityName) {
        if (files == null || files.length == 0) return;

        // validate all first
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) validateFile(file);
        }

        int displayOrder = (int) mediaAttachmentRepository.countByEntityTypeAndEntityIdAndIsActiveTrue(
                entityType, entityId);
        List<String> uploadedPublicIds = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;
                MediaRole role = displayOrder == 0 ? MediaRole.MAIN : MediaRole.GALLERY;
                MediaAsset asset = findOrUploadAsset(file, entityType, entityName);
                uploadedPublicIds.add(asset.getPublicId());
                MediaAttachment attachment = MediaAttachment
                        .builder()
                        .mediaAsset(asset)
                        .entityType(entityType)
                        .entityId(entityId)
                        .role(role)
                        .displayOrder(displayOrder)
                        .isActive(true)
                        .isDeleted(false)
                        .build();
                mediaAttachmentRepository.save(attachment);
                incrementReferenceCount(asset);
                displayOrder++;
            }
        } catch (Exception e) {
            // roll back uploaded assets
            uploadedPublicIds.forEach(publicId -> {
                try {
                    cloudinaryService.deleteFile(publicId);
                } catch (Exception ex) {
                    log.error("Failed to rollback uploaded media: {}", publicId, ex);
                }
            });
            throw new ApplicationException("400", "Batch upload failed: " + e.getMessage(),
                                           HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public void replaceProductmedias(Long productId, EntityType entityType, MultipartFile[] files) {
        // deactivate existing attachments
        List<MediaAttachment> existing = mediaAttachmentRepository.findAllByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAscIdAsc(
                entityType, productId);
        existing.forEach(att -> {
            att.setIsActive(false);
            att.setIsDeleted(true);
            // decrement reference count
            decrementReferenceCount(att.getMediaAsset());
        });
        mediaAttachmentRepository.saveAll(existing);

        saveMultimedias(productId, entityType, files, productId.toString());
    }

    @Override
    @Transactional
    public void deletemedias(Long entityId, EntityType type) {
        List<MediaAttachment> attachments = mediaAttachmentRepository.findAllByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAscIdAsc(
                type, entityId);
        attachments.forEach(att -> {
            att.setIsActive(false);
            att.setIsDeleted(true);
            decrementReferenceCount(att.getMediaAsset());
        });
        mediaAttachmentRepository.saveAll(attachments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MediaResponseDto> getmedias(Long entityId, EntityType type) {
        return mediaAttachmentRepository
                .findAllByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAscIdAsc(type,
                                                                                          entityId)
                .stream()
                .map(mediaMapper::mapMediaAttachmentToMediaResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MediaResponseDto getMainMedia(Long entityId, EntityType type) {
        MediaAttachment existing = mediaAttachmentRepository
                .findFirstByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAsc(type,
                                                                                       entityId)
                .orElseThrow(() -> new ApplicationException("404", "No media found for entityId=" +
                                                                   entityId + " and type=" + type,
                                                            HttpStatus.NOT_FOUND));

        return mediaMapper.mapMediaAttachmentToMediaResponseDto(existing);
    }

    /* -------------------- Helper methods -------------------- */

    @Override
    @Transactional(readOnly = true)
    public Map<Long, MediaResponseDto> getPrimarymedias(List<Long> entityIds, EntityType type) {
        if (entityIds == null || entityIds.isEmpty()) return Map.of();

        List<MediaAttachment> attachments = mediaAttachmentRepository.findAllByEntityTypeAndEntityIdInAndIsActiveTrueOrderByEntityIdAscDisplayOrderAsc(
                type, entityIds);
        Map<Long, MediaResponseDto> result = new LinkedHashMap<>();
        for (MediaAttachment att : attachments) {
            result.putIfAbsent(att.getEntityId(),
                               mediaMapper.mapMediaAttachmentToMediaResponseDto(att));
        }
        return result;
    }

    private MediaAsset findOrUploadAsset(MultipartFile file, EntityType entityType,
                                         String entityName) {
        try {
            String hash = computeSha256(file.getBytes());
            Optional<MediaAsset> existing = mediaAssetRepository.findByHashAndIsDeletedFalse(hash);
            if (existing.isPresent()) {
                log.debug("Found existing media asset by hash, reusing asset id={}", existing
                        .get()
                        .getId());
                return existing.get();
            }

            String folderName = (entityName == null || entityName.isBlank()) ? entityType
                                                                               .name()
                                                                               .toLowerCase() :
                                entityName.toLowerCase();
            CloudinaryUploadResponse uploadResponse = cloudinaryService.uploadFile(file,
                                                                                   "pricetag/" +
                                                                                   entityType
                                                                                           .name()
                                                                                           .toLowerCase() +
                                                                                   "/" +
                                                                                   folderName);
            registerRollbackCleanup(uploadResponse.getPublicId());

            MediaAsset asset = MediaAsset
                    .builder()
                    .publicId(uploadResponse.getPublicId())
                    .url(uploadResponse.getUrl())
                    .secureUrl(uploadResponse.getUrl())
                    .mimeType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .hash(hash)
                    .fileType("media")
                    .altText(entityName)
                    .isActive(true)
                    .isDeleted(false)
                    .referenceCount(0)
                    .build();

            return mediaAssetRepository.save(asset);
        } catch (IOException e) {
            throw new ApplicationException("500", "Failed to process file: " + e.getMessage(),
                                           HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void registerRollbackCleanup(String publicId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_ROLLED_BACK) return;
                try {
                    cloudinaryService.deleteFile(publicId);
                    log.info("Rolled back transaction, deleted uploaded media {}", publicId);
                } catch (Exception ex) {
                    log.error("Rollback cleanup failed for Cloudinary media {}: {}", publicId,
                              ex.getMessage());
                }
            }
        });
    }

//    private MediaResponseDto mapToMediaResponseDto(MediaAttachment attachment) {
//        if (attachment == null || attachment.getMediaAsset() == null) return null;
//        MediaAsset a = attachment.getMediaAsset();
//        return MediaResponseDto
//                .builder()
//                .attachmentId(attachment.getId())
//                .mediaAssetId(a.getId())
//                .url(a.getUrl())
//                .publicId(a.getPublicId())
//                .altText(a.getAltText())
//                .role(attachment.getRole() != null ? attachment
//                                                     .getRole()
//                                                     .name() : null)
//                .displayOrder(attachment.getDisplayOrder())
//                .metadata(attachment.getMetadata())
//                .build();
//    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new ApplicationException("400", "media file is required", HttpStatus.BAD_REQUEST);
        }
        if (file.isEmpty()) {
            throw new ApplicationException("400", "media file is empty", HttpStatus.BAD_REQUEST);
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new ApplicationException("400", "Invalid file type: " + contentType +
                                                  ". Allowed types: JPEG, PNG, WebP, GIF",
                                           HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            long sizeMB = file.getSize() / (1024 * 1024);
            long maxMB = MAX_FILE_SIZE / (1024 * 1024);
            throw new ApplicationException("400", "File size " + sizeMB +
                                                  "MB exceeds maximum allowed size of " + maxMB +
                                                  "MB", HttpStatus.BAD_REQUEST);
        }
    }

    private void incrementReferenceCount(MediaAsset asset) {
        if (asset == null) return;
        asset.setReferenceCount(Optional
                                        .ofNullable(asset.getReferenceCount())
                                        .orElse(0) + 1);
//        asset.setUpdatedAt(Instant.now());
        mediaAssetRepository.save(asset);
    }

    private void decrementReferenceCount(MediaAsset asset) {
        if (asset == null) return;
        int current = Optional
                .ofNullable(asset.getReferenceCount())
                .orElse(0);
        asset.setReferenceCount(Math.max(0, current - 1));
//        asset.setUpdatedAt(Instant.now());
        mediaAssetRepository.save(asset);
    }
}