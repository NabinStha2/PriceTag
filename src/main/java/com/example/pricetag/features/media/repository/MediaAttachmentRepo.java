package com.example.pricetag.features.media.repository;

import com.example.pricetag.enums.EntityType;
import com.example.pricetag.features.media.entity.MediaAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaAttachmentRepo extends JpaRepository<MediaAttachment, Long> {
    List<MediaAttachment> findAllByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAscIdAsc(
            EntityType entityType, Long entityId);


    Optional<MediaAttachment> findFirstByEntityTypeAndEntityIdAndIsActiveTrueOrderByDisplayOrderAsc(
            EntityType entityType, Long entityId);

    List<MediaAttachment> findAllByEntityTypeAndEntityIdInAndIsActiveTrueOrderByEntityIdAscDisplayOrderAsc(
            EntityType entityType, List<Long> entityIds);

    long countByEntityTypeAndEntityIdAndIsActiveTrue(EntityType entityType, Long entityId);

    long countByMediaAssetIdAndIsActiveTrue(Long mediaAssetId);

}
