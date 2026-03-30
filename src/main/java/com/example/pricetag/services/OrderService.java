package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    CommonResponseDto createOrder();
}
