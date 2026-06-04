package com.example.pricetag.features.media.repository;

import com.example.pricetag.features.media.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepo extends JpaRepository<MediaAsset, Long> {

    Optional<MediaAsset> findByHashAndIsDeletedFalse(String hash);

    Optional<MediaAsset> findByPublicIdAndIsActiveTrue(String publicId);

//    List<MediaAsset> findAllByEntityIdAndEntityType(Long entityId, EntityType entityType);
//
//    void deleteAllByEntityIdAndEntityType(Long entityId, EntityType entityType);

}