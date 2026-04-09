package com.example.pricetag.features.image.repository;

import com.example.pricetag.enums.ImageType;
import com.example.pricetag.features.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    List<Image> findAllByEntityIdAndEntityType(Long entityId, ImageType entityType);

    void deleteAllByEntityIdAndEntityType(Long entityId, ImageType entityType);

}