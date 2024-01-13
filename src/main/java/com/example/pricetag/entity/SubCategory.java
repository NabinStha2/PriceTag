package com.example.pricetag.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "subCategory")
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "subcategory_id")
  private Long id;

  @Column(name = "name")
  private String subCategoryName;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  @Builder.Default
  private List<Product> product = new ArrayList<>();

}
