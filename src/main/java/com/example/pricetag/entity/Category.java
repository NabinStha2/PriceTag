package com.example.pricetag.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "subCategories" })
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String categoryName;

  @JsonManagedReference
  @OneToMany(mappedBy = "category", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
  private List<SubCategory> subCategories;

  // Add @JsonIgnore here to prevent infinite recursion
  // @JsonIgnore
  // public List<SubCategory> getSubCategories() {
  // return subCategories;
  // }

}
