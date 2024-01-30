package com.example.pricetag.services.impl;

import com.cloudinary.Cloudinary;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.services.CloudinaryService;
import com.example.pricetag.utils.ColorLogger;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            System.out.println(uploadedFile);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException("500", "Failed to upload images", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Map delete(String id, String folderName) {
        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);
        try {
            ColorLogger.logInfo(id + folderName);
            return cloudinary.uploader().destroy(id, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
