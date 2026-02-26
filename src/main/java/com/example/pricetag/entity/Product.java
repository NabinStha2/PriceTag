package com.example.pricetag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties({"category", "subCategory"})
@Table(name = "products",
       indexes = {@Index(columnList = "subcategory_id",
                         name = "idx_subcategory_id"
       ), @Index(columnList = "category_id",
                 name = "idx_category_id"
       ), @Index(columnList = "brand",
                 name = "idx_brand"
       )},
       uniqueConstraints = @UniqueConstraint(columnNames = {"slug"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",
            nullable = false
    )
    private String name;

    // SEO-friendly URL slug
    @Column(nullable = false,
            unique = true
    )
    private String slug;  // Example: nike-air-max-red

    // Long description (HTML friendly)
    @Column(name = "description",
            nullable = false,
            columnDefinition = "longtext"
    )
    private String description;

    // Short description for listing
    @Column(length = 500)
    private String shortDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
                nullable = false
    )
    private Category category;

    //    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id",
                nullable = false
    )
    private SubCategory subCategory;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "brand")
    private String brand;

    @Column(name = "total_rating")
    private double totalRating = 0;

    @Column(name = "total_review")
    private Integer totalReview = 0;

    @Column(name = "base_price",
            nullable = false
    )
    private BigDecimal basePrice;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @OneToMany(mappedBy = "product",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<Variants> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<RatingReview> ratingReviews = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false,
            name = "created_at"
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
