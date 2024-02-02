package com.example.pricetag.services;

import com.example.pricetag.responses.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    CommonResponseDto createOrder();
}
