package com.example.pricetag.services;

import com.example.pricetag.dto.AddressDto;
import com.example.pricetag.dto.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    CommonResponseDto createAddress(AddressDto addressDto);

    CommonResponseDto getAddress();

}
