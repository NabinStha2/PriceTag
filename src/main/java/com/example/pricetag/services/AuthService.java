package com.example.pricetag.services;

import com.example.pricetag.config.AuthDetails;
import com.example.pricetag.dto.*;
import com.example.pricetag.entity.Otp;
import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.AuthRepository;
import com.example.pricetag.repository.OtpRepo;
import com.example.pricetag.repository.RefreshTokenRepo;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.utils.ColorLogger;
import com.example.pricetag.utils.EmailUtils;
import com.example.pricetag.utils.OtpGeneratorUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OtpGeneratorUtil otpGeneratorUtil;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private OtpRepo otpRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ColorLogger.logInfo("I am inside AuthService loadUserByUsername");
        Optional<User> user = authRepository.findByEmail(username);
        return user.map(AuthDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found " + username));
    }

    public CommonResponseDto register(RegisterUserDto registerUserDto) throws ApplicationException {
        ColorLogger
                .logInfo("I am inside AuthService register" + registerUserDto.toString());
        User existingUser = this.authRepository.findByEmail(registerUserDto.getEmail()).orElse(null);

        if (existingUser == null || !existingUser.isVerified()) {
            otpRepo.findByUser(existingUser).ifPresent(existingOtp -> otpRepo.delete(existingOtp));

            if (existingUser != null) {
                authRepository.delete(existingUser);
            }

            ColorLogger.logInfo("user :: " + registerUserDto.getPassword());

            User newUser = new User();
            newUser.setEmail(registerUserDto.getEmail());
            newUser.setName(registerUserDto.getFullName());
            newUser.setPhoneNumber(registerUserDto.getPhoneNumber());
            newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
            newUser.setAppUserRole(AppUserRole.ROLE_USER);
            newUser.setVerified(false);
            ColorLogger.logInfo("user :: " + newUser);


            Integer otpCode = otpGeneratorUtil.generateOTP(registerUserDto.getEmail());
            try {
                EmailDto newEmailDto = new EmailDto();
                newEmailDto.setRecipients(List.of(registerUserDto.getEmail()));
                newEmailDto.setSubject("Otp Code");
                newEmailDto.setBody("Your otp code is: " + otpCode);
                emailUtils.sendOtpEmail(newEmailDto);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }

            authRepository.save(newUser);
            Otp newOtp = new Otp();
            newOtp.setOtpCode(otpCode);
            newOtp.setUser(newUser);
            otpRepo.save(newOtp);

            return CommonResponseDto
                    .builder()
                    .success(true)
                    .message("Otp code has been sent successfully")
                    .build();

        } else {
            throw new ApplicationException("400", "Email already exists", HttpStatus.BAD_REQUEST);
        }
    }

    public CommonResponseDto verifyOtp(OtpDto otpDto) throws ApplicationException {
        ColorLogger
                .logInfo("I am inside AuthService verifyOtp");
        User existingUser = this.authRepository.findByEmail(otpDto.getEmail()).orElse(null);
        if (existingUser != null && !existingUser.isVerified()) {
            Optional<Otp> existingOtp = otpRepo.findByUser(existingUser);

            if (existingOtp.isPresent()) {
                if (existingOtp.get().getOtpCode() == otpDto.getOtpCode()) {
                    if (Duration.between(existingOtp.get().getOtpGenerationTime(),
                            LocalDateTime.now()).getSeconds() < (5 * 60)) {
                        otpRepo.delete(existingOtp.get());
                        existingUser.setVerified(true);
                        userRepo.save(existingUser);
                    } else {
                        otpRepo.delete(existingOtp.get());
                        throw new ApplicationException("400", "Otp Expired", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new ApplicationException("400", "Otp incorrect", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ApplicationException("404", "Otp not found", HttpStatus.NOT_FOUND);
            }

            RefreshToken refreshToken = refreshTokenService
                    .createRefreshToken(otpDto.getEmail());
            var jwtToken = jwtService.generateToken(otpDto.getEmail());
            AuthResponseDto authResponseDto = AuthResponseDto
                    .builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .build();
            return CommonResponseDto
                    .builder()
                    .data(authResponseDto)
                    .success(true)
                    .message("Login Successfully")
                    .build();
        } else {
            throw new ApplicationException("400", "Email already registered", HttpStatus.BAD_REQUEST);
        }
    }

    public AuthResponseDto getNewTokenByRefreshToken(RefreshTokenDto refreshTokenDto) throws ApplicationException {
        return refreshTokenService
                .findByToken(refreshTokenDto.getToken())
                .map(refreshTokenService::verifyRefreshTokenExpirationDate)
                .map(RefreshToken::getUser)
                .map(user -> {
                    ColorLogger.logInfo("refreshToken :: user: " + user);
                    String accessToken = jwtService.generateToken(user.getEmail());
                    return AuthResponseDto
                            .builder()
                            .accessToken(accessToken)
                            .build();
                }).orElseThrow(() -> new ApplicationException(null, "Refresh Token not found", HttpStatus.BAD_REQUEST));
    }

    public CommonResponseDto login(AuthDto authDto) throws ApplicationException {

        User user = this.userRepo.findByEmail(authDto.getEmail())
                .orElseThrow(() -> new ApplicationException("404", "Email not found",
                        HttpStatus.NOT_FOUND));
        ColorLogger.logError(user.toString());

        if (user.isVerified()) {
            Optional<RefreshToken> existingRefreshToken = refreshTokenService
                    .getRefreshTokenByUser(user);
            existingRefreshToken.ifPresent(token -> refreshTokenRepo.delete(existingRefreshToken.get()));

            RefreshToken refreshToken = refreshTokenService
                    .createRefreshToken(authDto.getEmail());

            AuthResponseDto authResponseDto = AuthResponseDto
                    .builder()
                    .accessToken(jwtService.generateToken(authDto.getEmail()))
                    .refreshToken(refreshToken.getToken())
                    .build();

            CommonResponseDto commonResponseDto = CommonResponseDto
                    .builder()
                    .data(authResponseDto)
                    .success(true)
                    .message("Login Successfully")
                    .build();

            System.out.println(commonResponseDto);

            return commonResponseDto;
        } else {
            throw new ApplicationException("400", "Account has been not verified. Please verify your account",
                    HttpStatus.BAD_REQUEST);
        }
    }

    public CommonResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto)
            throws ApplicationException {
        // try {
        ColorLogger
                .logInfo("I am inside AuthService forgotPassword");
        // String email = jwtService.extractUserName(jwtToken);
        User existingUser = authRepository.findByEmail(forgotPasswordDto.getEmail())
                .orElseThrow(() -> new ApplicationException("404", "Email not found",
                        HttpStatus.NOT_FOUND));
        if (existingUser != null) {
            Integer otpCode = otpGeneratorUtil.generateOTP(existingUser.getEmail());
            try {
                EmailDto newEmailDto = new EmailDto();
                newEmailDto.setRecipients(List.of(existingUser.getEmail()));
                newEmailDto.setSubject("Otp Code");
                newEmailDto.setBody("Your otp code is: " + otpCode);
                emailUtils.sendOtpEmail(newEmailDto);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }

            Otp existingOtp = otpRepo.findByUser(existingUser).orElse(null);

            if (existingOtp != null) {
                otpRepo.delete(existingOtp);
            }
            Otp newOtp = new Otp();
            newOtp.setOtpCode(otpCode);
            newOtp.setUser(existingUser);
            otpRepo.save(newOtp);

            return CommonResponseDto
                    .builder()
                    .message("Otp sent successfully")
                    .success(true)
                    .build();
        }
        // } catch (Exception e) {
        // ColorLogger.logError("I am inside forgotPassword Error :: " +
        // e.getMessage());
        // throw new ApplicationException("400", e.getMessage(),
        // HttpStatus.BAD_REQUEST);
        // }
        return null;
    }

    public CommonResponseDto verifyForgotPasswordOtp(OtpDto otpDto) throws ApplicationException {
        ColorLogger
                .logInfo("I am inside AuthService verifyForgotPasswordOtp");
        User existingUser = this.authRepository.findByEmail(otpDto.getEmail())
                .orElseThrow(() -> new ApplicationException("404", "Email not found",
                        HttpStatus.NOT_FOUND));
        if (existingUser != null && existingUser.isVerified()) {
            Optional<Otp> existingOtp = otpRepo.findByUser(existingUser);

            if (existingOtp.isPresent()) {
                if (existingOtp.get().getOtpCode() == otpDto.getOtpCode()) {
                    if (Duration.between(existingOtp.get().getOtpGenerationTime(),
                            LocalDateTime.now()).getSeconds() < (60)) {
                        otpRepo.delete(existingOtp.get());
                        existingUser.setPassword(passwordEncoder.encode(otpDto.getNewPassword()));
                        userRepo.save(existingUser);
                    } else {
                        otpRepo.delete(existingOtp.get());
                        throw new ApplicationException("400", "Otp Expired", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new ApplicationException("400", "Otp incorrect", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ApplicationException("404", "Otp not found", HttpStatus.NOT_FOUND);
            }

            return CommonResponseDto
                    .builder()
                    .message("Password reset successfully")
                    .success(true)
                    .build();

        } else {
            throw new ApplicationException("400", "Email already registered", HttpStatus.BAD_REQUEST);
        }
    }

    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        ColorLogger.logInfo("I am inside getCart :: " + userDetails.getUsername());
        User existingUser = userRepo.findByEmail(userDetails.getUsername()).orElse(null);
        if (existingUser == null) {
            throw new ApplicationException("404", "User not found", HttpStatus.NOT_FOUND);
        }
        return existingUser;
    }
}