package com.example.pricetag.controllers;

import com.example.pricetag.dto.AddCartItemDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("")
    public ResponseEntity<CommonResponseDto> getCart() {
        return ResponseEntity.ok(cartItemService.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<CommonResponseDto> createCart(@RequestBody AddCartItemDto addCartItemDto)
            throws ApplicationException {
        return ResponseEntity.ok(cartItemService.createCart(addCartItemDto));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CommonResponseDto> deleteCartItem(@PathVariable(name = "cartItemId") Long cartItemId)
            throws ApplicationException {
        return ResponseEntity.ok(cartItemService.deleteCartItem(cartItemId));
    }


}
