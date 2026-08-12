package com.airtribe.payflow.controller;

import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping
    public User registerUser(@RequestBody @NonNull User user) {
        return userService.registerUser(user);
    }

    // Return all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Return user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable @NonNull Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/upi/{upiId}")
    public Optional<User> getUserByUpiId(@PathVariable String upiId) {
        return userService.findByUpiId(upiId);
    }
}
