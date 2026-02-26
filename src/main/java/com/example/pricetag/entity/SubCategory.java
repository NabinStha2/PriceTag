package com.example.pricetag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString
//@RequiredArgsConstructor
@Builder
@Table(name = "sub_categories",
       indexes = {@Index(columnList = "category_id",
                         name = "idx_category_id"
       )},
       uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "name"})}
)
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties({"category", "product"})
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",
            nullable = false
    )
    private String subCategoryName;

    //    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
                nullable = false
    )
    private Category category;

    @OneToMany(mappedBy = "subCategory",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
    )
//    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

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

//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer()
//                .getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
//                .getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        SubCategory that = (SubCategory) o;
//        return getId() != null && Objects.equals(getId(), that.getId());
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
//                .getPersistentClass()
//                .hashCode() : getClass().hashCode();
//    }
}
