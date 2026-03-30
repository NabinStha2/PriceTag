package com.example.pricetag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"sub_categories"})
@Table(name = "categories",
        indexes = {@Index(columnList = "name",
                name = "idx_name"
        )}
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",
            nullable = false
    )
    private String categoryName;

    //    @JsonManagedReference
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>();

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false,
            name = "created_at"
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Add @JsonIgnore here to prevent infinite recursion
    // @JsonIgnore
    // public List<SubCategory> getSubCategories() {
    // return subCategories;
    // }

}
