package com.example.pricetag.controllers;

import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<CommonResponseDto> createOrder() {
        return new ResponseEntity<CommonResponseDto>(orderService.createOrder(), HttpStatus.CREATED);
    }
}
