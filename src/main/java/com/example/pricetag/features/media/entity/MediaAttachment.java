package com.example.pricetag.features.media.entity;

import com.example.pricetag.entity.BaseEntity;
import com.example.pricetag.enums.EntityType;
import com.example.pricetag.enums.MediaRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "media_attachment",
        indexes = {
                @Index(name = "idx_entity_type_id",
                        columnList = "entity_type, entity_id, is_active, display_order"),
                @Index(name = "idx_media_asset_id",
                        columnList = "media_asset_id")
        })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAttachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_asset_id",
            nullable = false)
    private MediaAsset mediaAsset;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type",
            length = 32,
            nullable = false)
    private EntityType entityType;

    @Column(name = "entity_id",
            nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",
            length = 32)
    private MediaRole role;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "metadata",
            columnDefinition = "text")
    private String metadata; // JSON string for flexible metadata

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}