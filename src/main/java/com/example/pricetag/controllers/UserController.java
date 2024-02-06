package com.example.pricetag.controllers;

import com.example.pricetag.entity.User;
import com.example.pricetag.responses.UserResponse;
import com.example.pricetag.services.UserService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserResponse> getMyUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(userService.getMyUser(userDetails.getUsername()));
    }

    @PatchMapping("/edit")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        ColorLogger.logInfo("I am inside updateUser Controller " + user);
        return ResponseEntity.ok(userService.updateUser(user));
    }

}
