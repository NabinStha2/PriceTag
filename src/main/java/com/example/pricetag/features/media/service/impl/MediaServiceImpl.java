package com.example.pricetag.features.media.service.impl;

import com.example.pricetag.features.category.repository.CategoryRepo;
import com.example.pricetag.features.cloudinary.service.CloudinaryService;
import com.example.pricetag.features.media.repository.MediaAssetRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png",
                                                                 "image/webp", "image/gif");
    private final CategoryRepo categoryRepo;
    private final CloudinaryService cloudinaryService;
    private final MediaAssetRepo mediaAssetRepo;

//
//
//    @Override
//    public ImageResponseDto saveSingleImage(Long entityId, EntityType entityType, MultipartFile file,
//                                            String entityName) {
//        ColorLogger.logInfo(
//                "Save single image for entityId " + entityId + " entity type " + entityType);
//        CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService.uploadFile(file,
//                                                                                         "pricetag/" +
//                                                                                         entityType
//                                                                                                 .name()
//                                                                                                 .toLowerCase() +
//                                                                                         "/" +
//                                                                                         entityName.toLowerCase());
//        registerRollbackCleanup(cloudinaryUploadResponse.getPublicId());
//
//        log.debug("CloudinaryUploadResponse = {} :: {}", cloudinaryUploadResponse.getPublicId(),
//                  cloudinaryUploadResponse.getUrl());
//
//        MediaAsset newMediaAsset = MediaAsset
//                .builder()
//                .entityId(entityId)
//                .entityType(entityType)
//                .imageUrl(cloudinaryUploadResponse.getUrl())
//                .imagePublicId(cloudinaryUploadResponse.getPublicId())
//                .build();
//
//        MediaAsset savedMediaAsset = mediaRepo.save(newMediaAsset);
//
//
//        return ImageResponseDto
//                .builder()
//                .id(savedMediaAsset.getId())
//                .url(savedMediaAsset.getImageUrl())
//                .publicId(savedMediaAsset.getImagePublicId())
//                .build();
//    }
//
//    private void registerRollbackCleanup(String publicId) {
//        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
//            return;
//        }
//
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCompletion(int status) {
//                if (status != STATUS_ROLLED_BACK) {
//                    return;
//                }
//
//                try {
//                    cloudinaryService.deleteFile(publicId);
//                    log.info("Rolled back transaction, deleted uploaded image {}", publicId);
//                } catch (Exception ex) {
//                    log.error("Rollback cleanup failed for Cloudinary image {}: {}", publicId,
//                              ex.getMessage());
//                }
//            }
//        });
//    }
//
//    @Override
//    public void saveMultiImages(Long entityId, EntityType entityType, MultipartFile[] files) {
//        for (MultipartFile file : files) {
//            CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService.uploadFile(file,
//                                                                                             "pricetag/" +
//                                                                                             entityType
//                                                                                                     .toString()
//                                                                                                     .toLowerCase());
//            MediaAsset newMediaAsset = MediaAsset
//                    .builder()
//                    .entityId(entityId)
//                    .entityType(entityType)
//                    .imagePublicId(cloudinaryUploadResponse.getPublicId())
//                    .imageUrl(cloudinaryUploadResponse.getUrl())
//                    .build();
//
//            mediaRepo.save(newMediaAsset);
//
//        }
//    }
//
//
//    @Override
//    public void replaceProductImages(Long productId, EntityType entityType, MultipartFile[] files) {
//        // 1️⃣ Delete existing images
//        deleteImages(productId, EntityType.PRODUCT);
//
//        // 2️⃣ Save new ones
//        saveMultiImages(productId, entityType, files);
//    }
//
//    @Override
//    public void deleteImages(Long entityId, EntityType type) {
//
//        List<MediaAsset> mediaAssets = mediaRepo.findAllByEntityIdAndEntityType(entityId, type);
//
//        // 1️⃣ Delete from Cloudinary FIRST
//        for (MediaAsset mediaAsset : mediaAssets) {
//            try {
//                cloudinaryService.deleteFile(mediaAsset.getImagePublicId());
//            } catch (Exception e) {
//                log.error("Failed to delete Cloudinary image: {} {}", mediaAsset.getImagePublicId(),
//                          e.getLocalizedMessage());
//            }
//        }
//
//        // 2️⃣ Delete from DB
//        mediaRepo.deleteAllByEntityIdAndEntityType(entityId, type);
//    }
//
//    @Override
//    public List<MediaAsset> getImages(Long entityId, EntityType type) {
//        return mediaRepo.findAllByEntityIdAndEntityType(entityId, type);
//    }
//
//
/// /    @Override
/// /    public ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto) {
/// /
/// /        ColorLogger.logError(imageDto.toString());
/// /        if (imageDto.getProductId() == null || imageDto.getFile() == null) {
/// /            return ResponseEntity.badRequest().build();
/// /        }
/// /        Product existingProduct = productRepo.findById(imageDto.getProductId())
/// /                .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));
/// /        String categoryName = existingProduct.getCategory().getCategoryName();
/// /        String subCategoryName = existingProduct.getSubCategory().getSubCategoryName();
/// /
/// /        List<Image> imageList = new ArrayList<>();
/// /
/// /       existingProduct.getImages().forEach(image -> cloudinaryService.deleteFile(image.getPublicId()));
/// /
/// /        for (MultipartFile file : imageDto.getFile()) {
/// /            CloudinaryUploadResponse cloudinaryUploadResponseData = cloudinaryService.uploadFile(file, "pricetag/" + categoryName + "/" + subCategoryName);
/// /            imageList.add(Image
/// /                    .builder()
/// /                    .imageUrl(cloudinaryUploadResponseData.getUrl())
/// /                    .imagePublicId(cloudinaryUploadResponseData.getPublicId())
/// /                    .build());
/// /        }
/// /
/// /        if (imageList.isEmpty()) {
/// /            return ResponseEntity.badRequest().build();
/// /        }
/// /        try {
/// /
/// /            List<Image> savedImages = imageRepo.saveAll(imageList);
/// /
/// /            ColorLogger.logInfo("Saved Images" + savedImages);
/// /
/// /            if (savedImages.isEmpty()) {
/// /                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
/// /            }
/// /
/// /            existingProduct.setImages(imageList);
/// /            productRepo.save(existingProduct);
/// /            return ResponseEntity.ok().body(CommonResponseDto
/// /                    .builder()
/// /                    .message("Image uploaded successfully")
/// /                    .success(true)
/// /                    .data(Map.of("url", imageList.stream().map(d -> d).collect(Collectors.toList())))
/// /                    .build());
/// /
/// /        } catch (DataAccessException ex) {
/// /            throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
/// /        }
/// /    }


}
