package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.UpdateProfileRequest;
import com.airtribe.task_master.dto.UserDto;
import com.airtribe.task_master.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserDto getProfile(Authentication authentication) {

        return userService.getProfile(authentication.getName());
    }

    @PutMapping("/profile")
    public UserDto updateProfile(Authentication authentication,
        @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(
            authentication.getName(),
            request.getEmail(),
            request.getFirstName(),
            request.getLastName()
        );
    }
}
