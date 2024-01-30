package com.example.pricetag.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"category", "subCategory"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "longtext")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "actual_price", nullable = false)
    private Double actualPrice;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @Column(name = "quantity")
    private Long quantity = 0L;

    @Column(name = "is_InStock")
    private Boolean isInStock = false;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


}
