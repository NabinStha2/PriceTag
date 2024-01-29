package com.example.pricetag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.Image;

import java.util.UUID;

@Repository
public interface ImageRepo extends JpaRepository<Image, UUID> {
}