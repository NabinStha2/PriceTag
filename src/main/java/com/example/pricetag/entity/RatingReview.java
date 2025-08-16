package com.example.pricetag.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rating_review")
public class RatingReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review")
    private String review;

    @ManyToOne
    private User user;

    @Column(name = "product_id")
    private String productId;


}
