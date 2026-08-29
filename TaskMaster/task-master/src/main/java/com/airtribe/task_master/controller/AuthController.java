package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.AuthResponse;
import com.airtribe.task_master.dto.LoginRequest;
import com.airtribe.task_master.dto.RefreshTokenRequest;
import com.airtribe.task_master.dto.RegisterRequest;
import com.airtribe.task_master.dto.UserDto;
import com.airtribe.task_master.entity.RefreshToken;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.repository.UserRepository;
import com.airtribe.task_master.security.JwtService;
import com.airtribe.task_master.service.RefreshTokenService;
import com.airtribe.task_master.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
        JwtService jwtService, UserRepository userRepository,
        RefreshTokenService refreshTokenService, UserDetailsService userDetailsService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(username);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, userDetails);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {

        RefreshToken oldRefreshToken =refreshTokenService.validateRefreshToken(
            request.getRefreshToken()
        );

        User user = oldRefreshToken.getUser();
        UserDetails userDetails =userDetailsService.loadUserByUsername(user.getUsername());

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(
            oldRefreshToken, userDetails
        );
        String newAccessToken = jwtService.generateAccessToken(user.getUsername());
        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
    }
}