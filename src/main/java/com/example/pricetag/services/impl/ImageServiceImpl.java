package com.example.pricetag.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pricetag.dto.ImageDto;
import com.example.pricetag.entity.Image;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.ImageRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CloudinaryService;
import com.example.pricetag.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

  @Autowired
  private CloudinaryService cloudinaryService;
  @Autowired
  private ImageRepo imageRepo;

  @Override
  public ResponseEntity<CommonResponseDto> uploadImage(ImageDto imageDto) {
    try {
      if (imageDto.getName().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      if (imageDto.getFile() != null) {
        return ResponseEntity.badRequest().build();
      }
      List<Image> imageList = new ArrayList<>();

      for (MultipartFile file : imageDto.getFile()) {
        imageList.add(Image
            .builder()
            .name(imageDto.getName())
            .url(cloudinaryService.uploadFile(file, "pricetag"))
            .build());

        // imageRepo.save(Image
        // .builder()
        // .name(imageDto.getName())
        // .url(cloudinaryService.uploadFile(file, "pricetag"))
        // .build());

      }

      if (imageList.isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      try {

        imageRepo.saveAll(imageList);

        

        return ResponseEntity.ok().body(CommonResponseDto
            .builder()
            .message("image uploaded successfully")
            .success(true)
            .data(Map.of("url", imageList.stream().map(d -> d).collect(Collectors.toList())))
            .build());
      } catch (DataAccessException ex) {
        throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
}
