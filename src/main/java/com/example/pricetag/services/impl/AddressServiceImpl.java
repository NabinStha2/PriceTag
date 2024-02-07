package com.example.pricetag.services.impl;

import com.example.pricetag.dto.AddressDto;
import com.example.pricetag.entity.Address;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.AddressService;
import com.example.pricetag.services.AuthService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    private final AuthService authService;
    private final UserRepo userRepo;

    public AddressServiceImpl(
            AuthService authService,
            UserRepo userRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @Override
    public CommonResponseDto createAddress(AddressDto addressDto) {
        User user = authService.getUser();

        try {
            if (user.getAddress() != null) {
                user.getAddress().setStreet(addressDto.getStreet());
                user.getAddress().setCity(addressDto.getCity());
                user.getAddress().setPostalCode(addressDto.getPostalCode());
                userRepo.save(user);
            } else {
                Address address = Address
                        .builder()
                        .street(addressDto.getStreet())
                        .city(addressDto.getCity())
                        .postalCode(addressDto.getPostalCode())
                        .build();
                user.setAddress(address);
                userRepo.save(user);
            }
            return CommonResponseDto
                    .builder()
                    .success(true)
                    .data(Map.of("results", user.getAddress()))
                    .message("Address created successfully")
                    .build();
        } catch (DataAccessException e) {
            throw new ApplicationException("500", "Address creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CommonResponseDto getAddress() {
        User user = authService.getUser();

        if (user.getAddress() == null) {
            throw new ApplicationException("404", "Address not found", HttpStatus.NOT_FOUND);
        }

        return CommonResponseDto
                .builder()
                .success(true)
                .data(Map.of("results", user.getAddress()))
                .message("Address fetched successfully")
                .build();
    }


}
