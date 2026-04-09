package com.example.pricetag.features.image.entity;

import com.example.pricetag.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images",
        indexes = {@Index(columnList = "entity_id, entity_type", name = "idx_entity_id_type")})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_public_id")
    private String imagePublicId;

    // 🔥 Generic reference
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private ImageType entityType;
}
