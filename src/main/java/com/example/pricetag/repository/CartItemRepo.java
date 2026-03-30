package com.example.pricetag.repository;

import com.example.pricetag.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUserId(Long userId);

    CartItem findByProductId(Long productId);

}
