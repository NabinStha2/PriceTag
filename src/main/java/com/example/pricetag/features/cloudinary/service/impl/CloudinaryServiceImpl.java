package com.example.pricetag.features.cloudinary.service.impl;

import com.cloudinary.Cloudinary;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.cloudinary.dto.response.CloudinaryDeleteResponse;
import com.example.pricetag.features.cloudinary.dto.response.CloudinaryUploadResponse;
import com.example.pricetag.features.cloudinary.service.CloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResponse uploadFile(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folder));
            return CloudinaryUploadResponse.builder()
                    .publicId(uploadResult.get("public_id").toString())
                    .url(uploadResult.get("secure_url").toString()) // use secure_url for https
                    .build();
        } catch (IOException e) {
            throw new ApplicationException("500", "Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CloudinaryDeleteResponse deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, Map.of());
            return CloudinaryDeleteResponse.builder()
                    .result(result.get("result").toString())
                    .build();
        } catch (IOException e) {
            throw new ApplicationException("500", "Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}