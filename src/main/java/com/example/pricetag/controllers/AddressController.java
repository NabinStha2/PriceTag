package com.example.pricetag.controllers;

import com.example.pricetag.dto.AddressDto;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @PostMapping
    private ResponseEntity<CommonResponseDto> createAddress(@RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.createAddress(addressDto));
    }

    @GetMapping
    private ResponseEntity<CommonResponseDto> getAddress() {
        return ResponseEntity.ok(addressService.getAddress());
    }

}
