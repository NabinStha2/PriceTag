package com.example.pricetag.services.impl;

import com.example.pricetag.dto.ImageDto;
import com.example.pricetag.entity.Image;
import com.example.pricetag.entity.Product;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.ImageRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CloudinaryService;
import com.example.pricetag.services.ImageService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto) {

        ColorLogger.logError(imageDto.toString());
        if (imageDto.getProductId() == null || imageDto.getFile() == null) {
            return ResponseEntity.badRequest().build();
        }
        Product existingProduct = productRepo.findById(imageDto.getProductId())
                .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));
        String categoryName = existingProduct.getCategory().getCategoryName();
        String subCategoryName = existingProduct.getSubCategory().getSubCategoryName();

        List<Image> imageList = new ArrayList<>();

        existingProduct.getImages().forEach(image -> cloudinaryService.delete(image.getPublicId(),
                "pricetag/" + categoryName
                        + "/"
                        + subCategoryName));

        for (MultipartFile file : imageDto.getFile()) {
            String url = cloudinaryService.uploadFile(file, "pricetag/" + categoryName + "/" + subCategoryName);
            ColorLogger.logInfo(url.split("/")[url.split("/").length - 1]);
            imageList.add(Image
                    .builder()
                    .url(url)
                    .publicId("pricetag/" + categoryName + "/" + subCategoryName + "/" + url.split("/")[url.split("/").length - 1])
                    .build());
        }

        if (imageList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {

            List<Image> savedImages = imageRepo.saveAll(imageList);

            ColorLogger.logInfo("Saved Images" + savedImages);

            if (savedImages.isEmpty()) {
                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            existingProduct.setImages(imageList);
            productRepo.save(existingProduct);
            return ResponseEntity.ok().body(CommonResponseDto
                    .builder()
                    .message("Image uploaded successfully")
                    .success(true)
                    .data(Map.of("url", imageList.stream().map(d -> d).collect(Collectors.toList())))
                    .build());

        } catch (DataAccessException ex) {
            throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
