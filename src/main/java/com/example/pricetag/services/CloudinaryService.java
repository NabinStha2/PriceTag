package com.example.pricetag.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {

    String uploadFile(MultipartFile file, String folderName);


    Map delete(String id);

}
