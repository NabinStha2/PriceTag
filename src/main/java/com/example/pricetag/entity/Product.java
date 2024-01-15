package com.example.pricetag.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "category", "subCategory" })
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @JsonManagedReference
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "subcategory_id")
  private SubCategory subCategory;

  @Column(name = "actual_price", nullable = false)
  private Double actualPrice;

  @Column(name = "discounted_price")
  private Double discountedPrice;

}
