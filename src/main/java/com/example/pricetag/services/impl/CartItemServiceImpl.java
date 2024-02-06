package com.example.pricetag.services.impl;

import com.example.pricetag.dto.*;
import com.example.pricetag.entity.CartItem;
import com.example.pricetag.entity.Product;
import com.example.pricetag.entity.User;
import com.example.pricetag.entity.Variants;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CartItemRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.repository.VariantRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.services.CartItemService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private VariantRepo variantRepo;

    @Override
    public CommonResponseDto getCart() {
        User existingUser = authService.getUser();

        List<CartItemDto> listOfCartItemDto = new ArrayList<>();

        existingUser
                .getCartItems()
                .stream()
                .sorted(Comparator.comparing(CartItem::getCreatedAt).reversed()).toList()
                .forEach(cartItem -> {
                    if (cartItem.getCheckoutAmt() == null) {
                        listOfCartItemDto.add(CartItemDto
                                .builder()
                                .id(cartItem.getId())
                                .product(ProductDto
                                        .builder()
                                        .name(cartItem.getProduct().getName())
                                        .description(cartItem.getProduct().getDescription())
                                        .images(cartItem.getProduct().getImages())
                                        .productId(cartItem.getProduct().getId())
                                        .createdAt(cartItem.getProduct().getCreatedAt())
                                        .updatedAt(cartItem.getProduct().getUpdatedAt())
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
                                .checkoutAmt(cartItem.getCheckoutAmt())
                                .variants(cartItem.getVariants())
                                .build());
                    }
                });

        return CommonResponseDto
                .builder()
                .message("Cart fetched successfully")
                .data(Map.of("results", listOfCartItemDto))
                .success(true)
                .build();

    }

    @Override
    public CommonResponseDto createCart(AddCartItemDto addCartItemDto) {
        User existingUser = authService.getUser();
        ColorLogger.logInfo("I am inside getCart :: " + existingUser);

        Optional<Product> existingProduct = productRepo.findById(addCartItemDto.getProductId());
        if (existingProduct.isPresent()) {
            Optional<Variants> variantPresent = Optional.empty();
            for (Variants variant : existingProduct.get().getVariants()) {
                if (variant.getId().equals(addCartItemDto.getVariantId())) {
                    variantPresent = Optional.of(variant);
                    break;
                }
            }
            if (variantPresent.isEmpty()) {
                throw new ApplicationException("404", "Variant not found inside given product id", HttpStatus.NOT_FOUND);
            }

            Optional<Variants> existingVariant = variantRepo.findById(addCartItemDto.getVariantId());
            if (existingVariant.isEmpty()) {
                throw new ApplicationException("404", "Variant not found", HttpStatus.NOT_FOUND);
            }

            if (addCartItemDto.getQuantity() > existingVariant.get().getQuantity()) {
                throw new ApplicationException("400", "Product quantity not available", HttpStatus.BAD_REQUEST);
            }

            List<CartItem> listOfCartItem = existingUser.getCartItems();

            Optional<CartItem> filteredCartItem = listOfCartItem.stream().filter((cartItem) -> {
                if (cartItem.getProduct().getId().equals(existingProduct.get().getId()))
                    return cartItem.getCheckoutAmt() == null && cartItem.getVariants().getId().equals(existingVariant.get().getId());
                return false;
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
                        .variants(existingVariant.get())
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

    public CommonResponseDto deleteCartItem(Long cartItemId) {
        User existingUser = authService.getUser();

        List<CartItem> listOfCartItems = existingUser.getCartItems();

        Optional<CartItem> filteredCartItem = existingUser.getCartItems().stream().filter(cartItem -> {
            boolean isPresent = cartItem.getId().equals(cartItemId);
            if (isPresent) {
                listOfCartItems.remove(cartItem);
            }
            return isPresent;
        }).findFirst();

        if (filteredCartItem.isPresent()) {
            existingUser.setCartItems(listOfCartItems);
            cartItemRepo.delete(filteredCartItem.get());
            userRepo.save(existingUser);

            return CommonResponseDto
                    .builder()
                    .message("Cart item deleted successfully")
                    .data(Map.of("results", filteredCartItem.get()))
                    .success(true)
                    .build();
        } else {
            throw new ApplicationException("404", "Delete of cart item id doesn't present inside user cart", HttpStatus.NOT_FOUND);
        }
    }


}
