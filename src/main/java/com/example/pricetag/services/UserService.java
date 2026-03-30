package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.entity.User;
import com.example.pricetag.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserResponse getMyUser(String email);

    UserResponse findUserById(int id);

    CommonResponseDto deleteUser(String email);

    UserResponse updateUser(User user);

}
