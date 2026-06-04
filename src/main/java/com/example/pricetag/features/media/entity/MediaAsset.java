package com.example.pricetag.features.media.entity;

import com.example.pricetag.entity.BaseEntity;
import com.example.pricetag.enums.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "media_asset",
        indexes = {@Index(columnList = "hash",
                name = "idx_media_hash"), @Index(columnList = "public_id",
                name = "idx_media_public_id")})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaAsset extends BaseEntity {

    @Column(name = "url",
            nullable = false)
    private String url;

    @Column(name = "public_id",
            nullable = false,
            unique = true)
    private String publicId;

    @Column(name = "secure_url")
    private String secureUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "media_type")
    private MediaType mediaType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    /**
     * SHA-256 hex of file bytes. Used for deduplication.
     */
    @Column(name = "hash",
            length = 120)
    private String hash;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "alt_text")
    private String altText;

    @Builder.Default
    @Column(name = "reference_count")
    private Integer referenceCount = 0;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
