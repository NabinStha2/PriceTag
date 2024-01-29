package com.example.pricetag.services.impl;

import com.example.pricetag.dto.*;
import com.example.pricetag.entity.CartItem;
import com.example.pricetag.entity.Product;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CartItemRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CartItemService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public CommonResponseDto getCart() {
        User existingUser = getUser();

        List<CartItemDto> listOfCartItemDtos = new ArrayList<>();

        existingUser.getCartItems().forEach(cartItem -> {
            listOfCartItemDtos.add(CartItemDto
                    .builder()
                    .id(cartItem.getId())
                    .product(ProductDto
                            .builder()
                            .name(cartItem.getProduct().getName())
                            .actualPrice(cartItem.getProduct().getActualPrice())
                            .discountedPrice(cartItem.getProduct().getDiscountedPrice())
                            .description(cartItem.getProduct().getDescription())
                            .images(cartItem.getProduct().getImages())
                            .productId(cartItem.getProduct().getId())
                            .category(CategoryDto
                                    .builder()
                                    .id(cartItem.getProduct().getCategory().getId())
                                    .categoryName(cartItem.getProduct().getCategory().getCategoryName())
                                    .build())
                            .subCategory(SubCategoryDto
                                    .builder()
                                    .id(cartItem.getProduct().getSubCategory().getId())
                                    .subCategoryName(cartItem.getProduct().getSubCategory().getSubCategoryName())
                                    .build())
                            .build())
                    .user(existingUser)
                    .quantity(cartItem.getQuantity())
                    .createdAt(cartItem.getCreatedAt())
                    .updatedAt(cartItem.getUpdatedAt())
                    .build());
        });

        return CommonResponseDto
                .builder()
                .message("Cart fetched successfully")
                .data(Map.of("results", listOfCartItemDtos))
                .success(true)
                .build();

    }

    @Override
    public CommonResponseDto createCart(AddCartItemDto addCartItemDto) {
        User existingUser = getUser();
        ColorLogger.logInfo("I am inside getCart :: " + existingUser);

        Optional<Product> existingProduct = productRepo.findById(addCartItemDto.getProductId());
        if (existingProduct.isPresent()) {
            List<CartItem> listOfCartItem = existingUser.getCartItems();

            Optional<CartItem> filteredCartItem = listOfCartItem.stream().filter((cartItem) -> {
                return cartItem.getProduct().getId().equals(existingProduct.get().getId());
            }).findFirst();

            if (filteredCartItem.isPresent()) {
                CartItem existingCartItem = filteredCartItem.get();
                existingCartItem.setQuantity(addCartItemDto.getQuantity());
                cartItemRepo.save(existingCartItem);
                return CommonResponseDto
                        .builder()
                        .message("Product added to cart successfully")
                        .data(Map.of("results", existingCartItem))
                        .success(true)
                        .build();
            } else {
                CartItem newCartItem = CartItem
                        .builder()
                        .user(existingUser)
                        .product(existingProduct.get())
                        .quantity(addCartItemDto.getQuantity())
                        .build();
                CartItem savedCartItem = cartItemRepo.save(newCartItem);
                existingUser.getCartItems().add(savedCartItem);
                userRepo.save(existingUser);
                return CommonResponseDto
                        .builder()
                        .message("Product created to cart successfully")
                        .data(Map.of("results", newCartItem))
                        .success(true)
                        .build();
            }

        } else {
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }

    private User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        ColorLogger.logInfo("I am inside getCart :: " + userDetails.getUsername());
        User existingUser = userRepo.findByEmail(userDetails.getUsername()).orElse(null);
        if (existingUser == null) {
            throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
        }
        return existingUser;
    }
}
