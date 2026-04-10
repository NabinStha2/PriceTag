package com.example.pricetag.features.image.service.impl;

import com.example.pricetag.enums.ImageType;
import com.example.pricetag.features.category.repository.CategoryRepo;
import com.example.pricetag.features.cloudinary.dto.response.CloudinaryUploadResponse;
import com.example.pricetag.features.cloudinary.service.CloudinaryService;
import com.example.pricetag.features.image.dto.response.ImageResponseDto;
import com.example.pricetag.features.image.entity.Image;
import com.example.pricetag.features.image.repository.ImageRepo;
import com.example.pricetag.features.image.service.ImageService;
import com.example.pricetag.utils.ColorLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageRepo imageRepo;

    @Override
    public ImageResponseDto saveSingleImage(Long entityId, ImageType entityType, MultipartFile file,
                                            String entityName) {
        ColorLogger.logInfo("Save single image for entityId " + entityId + " entity type " + entityType);
        CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService.uploadFile(file, "pricetag/" + entityType
                .name()
                .toLowerCase() + "/" + entityName.toLowerCase());
        registerRollbackCleanup(cloudinaryUploadResponse.getPublicId());

        log.debug("CloudinaryUploadResponse = {} :: {}", cloudinaryUploadResponse.getPublicId(),
                  cloudinaryUploadResponse.getUrl());

        Image newImage = Image
                .builder()
                .entityId(entityId)
                .entityType(entityType)
                .imageUrl(cloudinaryUploadResponse.getUrl())
                .imagePublicId(cloudinaryUploadResponse.getPublicId())
                .build();

        Image savedImage = imageRepo.save(newImage);


        return ImageResponseDto
                .builder()
                .id(savedImage.getId())
                .url(savedImage.getImageUrl())
                .publicId(savedImage.getImagePublicId())
                .build();
    }

    private void registerRollbackCleanup(String publicId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_ROLLED_BACK) {
                    return;
                }

                try {
                    cloudinaryService.deleteFile(publicId);
                    log.info("Rolled back transaction, deleted uploaded image {}", publicId);
                } catch (Exception ex) {
                    log.error("Rollback cleanup failed for Cloudinary image {}: {}", publicId, ex.getMessage());
                }
            }
        });
    }

    @Override
    public void saveMultiImages(Long entityId, ImageType entityType, MultipartFile[] files) {
        for (MultipartFile file : files) {
            CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService.uploadFile(file, "pricetag/" +
                                                                                                   entityType
                                                                                                           .toString()
                                                                                                           .toLowerCase());
            Image newImage = Image
                    .builder()
                    .entityId(entityId)
                    .entityType(entityType)
                    .imagePublicId(cloudinaryUploadResponse.getPublicId())
                    .imageUrl(cloudinaryUploadResponse.getUrl())
                    .build();

            imageRepo.save(newImage);

        }
    }


    @Override
    public void replaceProductImages(Long productId, ImageType entityType, MultipartFile[] files) {
        // 1️⃣ Delete existing images
        deleteImages(productId, ImageType.PRODUCT);

        // 2️⃣ Save new ones
        saveMultiImages(productId, entityType, files);
    }

    @Override
    public void deleteImages(Long entityId, ImageType type) {

        List<Image> images = imageRepo.findAllByEntityIdAndEntityType(entityId, type);

        // 1️⃣ Delete from Cloudinary FIRST
        for (Image image : images) {
            try {
                cloudinaryService.deleteFile(image.getImagePublicId());
            } catch (Exception e) {
                log.error("Failed to delete Cloudinary image: {} {}", image.getImagePublicId(),
                          e.getLocalizedMessage());
            }
        }

        // 2️⃣ Delete from DB
        imageRepo.deleteAllByEntityIdAndEntityType(entityId, type);
    }

    @Override
    public List<Image> getImages(Long entityId, ImageType type) {
        return imageRepo.findAllByEntityIdAndEntityType(entityId, type);
    }


//    @Override
//    public ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto) {
//
//        ColorLogger.logError(imageDto.toString());
//        if (imageDto.getProductId() == null || imageDto.getFile() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        Product existingProduct = productRepo.findById(imageDto.getProductId())
//                .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));
//        String categoryName = existingProduct.getCategory().getCategoryName();
//        String subCategoryName = existingProduct.getSubCategory().getSubCategoryName();
//
//        List<Image> imageList = new ArrayList<>();
//
//       existingProduct.getImages().forEach(image -> cloudinaryService.deleteFile(image.getPublicId()));
//
//        for (MultipartFile file : imageDto.getFile()) {
//            CloudinaryUploadResponse cloudinaryUploadResponseData = cloudinaryService.uploadFile(file, "pricetag/" + categoryName + "/" + subCategoryName);
//            imageList.add(Image
//                    .builder()
//                    .imageUrl(cloudinaryUploadResponseData.getUrl())
//                    .imagePublicId(cloudinaryUploadResponseData.getPublicId())
//                    .build());
//        }
//
//        if (imageList.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//
//            List<Image> savedImages = imageRepo.saveAll(imageList);
//
//            ColorLogger.logInfo("Saved Images" + savedImages);
//
//            if (savedImages.isEmpty()) {
//                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//            existingProduct.setImages(imageList);
//            productRepo.save(existingProduct);
//            return ResponseEntity.ok().body(CommonResponseDto
//                    .builder()
//                    .message("Image uploaded successfully")
//                    .success(true)
//                    .data(Map.of("url", imageList.stream().map(d -> d).collect(Collectors.toList())))
//                    .build());
//
//        } catch (DataAccessException ex) {
//            throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


}
