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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final AuthService authService;
    private final VariantRepo variantRepo;

    public CartItemServiceImpl(CartItemRepo cartItemRepo, UserRepo userRepo, ProductRepo productRepo, AuthService authService, VariantRepo variantRepo) {
        this.cartItemRepo = cartItemRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.authService = authService;
        this.variantRepo = variantRepo;
    }

    private static CartItemDto buildCartItemDto(CartItem newCartItem, User existingUser) {
        return CartItemDto
                .builder()
                .id(newCartItem.getId())
                .product(ProductDto
                        .builder()
                        .name(newCartItem.getProduct().getName())
                        .description(newCartItem.getProduct().getDescription())
                        .images(newCartItem.getProduct().getImages())
                        .productId(newCartItem.getProduct().getId())
                        .createdAt(newCartItem.getProduct().getCreatedAt())
                        .updatedAt(newCartItem.getProduct().getUpdatedAt())
                        .category(CategoryDto
                                .builder()
                                .id(newCartItem.getProduct().getCategory().getId())
                                .categoryName(newCartItem.getProduct().getCategory().getCategoryName())
                                .build())
                        .subCategory(SubCategoryDto
                                .builder()
                                .id(newCartItem.getProduct().getSubCategory().getId())
                                .subCategoryName(newCartItem.getProduct().getSubCategory().getSubCategoryName())
                                .build())
                        .build())
                .user(existingUser)
                .quantity(newCartItem.getQuantity())
                .createdAt(newCartItem.getCreatedAt())
                .updatedAt(newCartItem.getUpdatedAt())
                .checkoutAmt(newCartItem.getCheckoutAmt())
                .variants(newCartItem.getVariants())
                .build();
    }

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
                        listOfCartItemDto.add(buildCartItemDto(cartItem, existingUser));
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
        // Get the current user
        User existingUser = authService.getUser();

        // Log user information
        ColorLogger.logInfo("I am inside getCart :: " + existingUser);

        // Check if the product exists
        Optional<Product> existingProduct = productRepo.findById(addCartItemDto.getProductId());
        if (existingProduct.isPresent()) {
            // Check if the variant exists within the product
            Optional<Variants> variantPresent = existingProduct.get().getVariants().stream()
                    .filter(variant -> variant.getId().equals(addCartItemDto.getVariantId()))
                    .findFirst();
            if (variantPresent.isEmpty()) {
                throw new ApplicationException("404", "Variant not found inside given product id", HttpStatus.NOT_FOUND);
            }

            // Check if the variant exists
            Optional<Variants> existingVariant = variantRepo.findById(addCartItemDto.getVariantId());
            if (existingVariant.isEmpty()) {
                throw new ApplicationException("404", "Variant not found", HttpStatus.NOT_FOUND);
            }

            // Check if the requested quantity is available
            if (addCartItemDto.getQuantity() > existingVariant.get().getQuantity()) {
                throw new ApplicationException("400", "Product quantity not available", HttpStatus.BAD_REQUEST);
            }

            // Get the list of existing cart items for the user
            List<CartItem> listOfCartItem = existingUser.getCartItems();

            // Check if the cart item already exists
            Optional<CartItem> filteredCartItem = listOfCartItem.stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(existingProduct.get().getId())
                            && cartItem.getCheckoutAmt() == null
                            && cartItem.getVariants().getId().equals(existingVariant.get().getId()))
                    .findFirst();

            if (filteredCartItem.isPresent()) {
                // Update existing cart item quantity
                CartItem existingCartItem = filteredCartItem.get();
                existingCartItem.setQuantity(addCartItemDto.getQuantity());
                cartItemRepo.save(existingCartItem);
                return CommonResponseDto
                        .builder()
                        .message("Product added to cart successfully")
                        .data(Map.of("results", buildCartItemDto(existingCartItem, existingUser)))
                        .success(true)
                        .build();
            } else {
                // Create a new cart item
                CartItem newCartItem = CartItem
                        .builder()
                        .user(existingUser)
                        .product(existingProduct.get())
                        .quantity(addCartItemDto.getQuantity())
                        .variants(existingVariant.get())
                        .build();
                CartItem savedCartItem = cartItemRepo.save(newCartItem);

                // Update user cart items and save user
                existingUser.getCartItems().add(savedCartItem);
                userRepo.save(existingUser);

                // Return success response
                return CommonResponseDto
                        .builder()
                        .message("Product created to cart successfully")
                        .data(Map.of("results", buildCartItemDto(newCartItem, existingUser)))
                        .success(true)
                        .build();
            }

        } else {
            // Product not found
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
