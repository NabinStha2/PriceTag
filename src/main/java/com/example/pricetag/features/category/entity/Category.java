package com.example.pricetag.features.category.entity;

import com.example.pricetag.features.subcategory.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories",
        indexes = {@Index(columnList = "name",
                name = "idx_category_name"), @Index(columnList = "name, is_deleted",
                name = "idx_category_name_deleted")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",
            nullable = false,
            length = 100)  // ✅ removed duplicate unique
    private String categoryName;

    // Denormalized from Image entity for fast reads — kept in sync by ImageService
    @Column(name = "image_url",
            length = 500)               // ✅ safe URL length
    private String imageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>();

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false,
            name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}