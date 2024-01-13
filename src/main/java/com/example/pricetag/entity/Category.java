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
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "category_id")
  private Long id;

  @Column(name = "name")
  private String categoryName;

  @OneToMany
  @JoinColumn(name = "subcategory_id")
  @Builder.Default
  private List<SubCategory> subCategory = new ArrayList<>();

}
