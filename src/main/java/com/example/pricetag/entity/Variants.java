package com.example.pricetag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "variants")
public class Variants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> size = new ArrayList<>();

    @Column(name = "color")
    private Color color;

    @Column(name = "quantity")
    private Long quantity = 0L;

    @Column(name = "actual_price", nullable = false)
    private Double actualPrice;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

}
