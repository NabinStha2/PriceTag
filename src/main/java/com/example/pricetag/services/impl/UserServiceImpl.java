package com.example.pricetag.services.impl;

import com.example.pricetag.entity.CartItem;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.responses.UserResponse;
import com.example.pricetag.services.UserService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserResponse getMyUser(String email) throws ApplicationException {
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .cartItems(user.getCartItems().stream().sorted(Comparator.comparing(CartItem::getCreatedAt).reversed()).toList())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .appUserRole(user.getAppUserRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userResponse;

    }

    @Override
    public UserResponse findUserById(int id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
        }

        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .appUserRole(user.getAppUserRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        return userResponse;

    }

    @Override
    public CommonResponseDto deleteUser(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
        }
        userRepo.delete(user);
        return CommonResponseDto
                .builder()
                .message("User has been deleted")
                .success(true)
                .build();

    }

    @Override
    public UserResponse updateUser(User user) {
        try {
            ColorLogger.logInfo("I am inside updateUser :: " + user.getEmail());

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();

            User existingUser = userRepo.findByEmail(userDetails.getUsername()).orElse(null);

            if (existingUser == null) {
                throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
            }

            existingUser.setName(user.getName() == null ? existingUser.getName() : user.getName());
            existingUser
                    .setPhoneNumber(user.getPhoneNumber() == null ? existingUser.getPhoneNumber() : user.getPhoneNumber());

            userRepo.save(existingUser);
            UserResponse userResponse = UserResponse
                    .builder()
                    .id(existingUser.getId())
                    .email(existingUser.getEmail())
                    .name(existingUser.getName())
                    .phoneNumber(existingUser.getPhoneNumber())
                    .appUserRole(existingUser.getAppUserRole())
                    .createdAt(existingUser.getCreatedAt())
                    .updatedAt(existingUser.getUpdatedAt())
                    .build();
            return userResponse;
        } catch (Exception e) {
            throw new ApplicationException("500", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
