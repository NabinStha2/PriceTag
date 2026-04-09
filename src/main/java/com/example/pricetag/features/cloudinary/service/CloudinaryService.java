package com.example.pricetag.features.cloudinary.service;

import com.example.pricetag.features.cloudinary.dto.response.CloudinaryDeleteResponse;
import com.example.pricetag.features.cloudinary.dto.response.CloudinaryUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    CloudinaryUploadResponse uploadFile(MultipartFile file, String folderName);


    CloudinaryDeleteResponse deleteFile(String publicId);
}
