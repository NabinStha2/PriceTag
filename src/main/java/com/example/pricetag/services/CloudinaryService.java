package com.example.pricetag.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

  String uploadFile(MultipartFile file, String folderName);

}
