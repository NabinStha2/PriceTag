package com.example.pricetag.services;

import com.example.pricetag.dto.RegisterUserDto;
import com.example.pricetag.entity.Role;
import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.AuthRepository;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserRegistrationService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @Autowired
    public UserRegistrationService(AuthRepository authRepository, PasswordEncoder passwordEncoder, OtpService otpService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    public User registerNewUser(RegisterUserDto registerUserDto) throws ApplicationException {
        ColorLogger.logInfo("Registering new user: " + registerUserDto.toString());

        User existingUser = authRepository.findByEmail(registerUserDto.getEmail()).orElse(null);

        if (existingUser != null && existingUser.isVerified()) {
            throw new ApplicationException("400", "Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (existingUser != null) {
            authRepository.delete(existingUser);
        }

        User newUser = new User();
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setName(registerUserDto.getFullName());
        newUser.setPhoneNumber(registerUserDto.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        newUser.setRoles(Set.of(Role.builder().name(AppUserRole.ROLE_USER.name()).build()));
        newUser.setVerified(false);

        ColorLogger.logInfo("Created new user: " + newUser);

        return authRepository.save(newUser);
    }

    public void initiateRegistration(RegisterUserDto registerUserDto) throws ApplicationException {
        User newUser = registerNewUser(registerUserDto);
        Integer otpCode = otpService.generateAndSendOtp(registerUserDto.getEmail());
        otpService.saveOtp(newUser, otpCode);
    }
}
