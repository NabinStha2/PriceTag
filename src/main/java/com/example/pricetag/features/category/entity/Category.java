package com.example.pricetag.features.category.entity;

import com.example.pricetag.entity.BaseEntity;
import com.example.pricetag.features.subcategory.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
        indexes = {
                @Index(name = "idx_category_name",
                        columnList = "name"),
                @Index(name = "idx_category_name_deleted",
                        columnList = "name, is_deleted"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        })
public class Category extends BaseEntity {

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

}