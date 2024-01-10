package com.example.pricetag.services;

import org.springframework.stereotype.Service;

import com.example.pricetag.entity.User;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.responses.UserResponse;

@Service
public interface UserService {

  public UserResponse getMyUser(String email);

  public UserResponse findUserById(int id);

  public CommonResponseDto deleteUser(String email);

  public UserResponse updateUser(User user);

}
