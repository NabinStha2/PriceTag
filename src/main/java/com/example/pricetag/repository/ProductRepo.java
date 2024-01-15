package com.example.pricetag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}
