package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private Long productId;
    private MultipartFile[] file;
    private Date createdAt;
    private Date updatedAt;
}
