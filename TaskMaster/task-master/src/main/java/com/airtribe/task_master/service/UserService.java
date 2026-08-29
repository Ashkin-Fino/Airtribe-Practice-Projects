package com.airtribe.task_master.service;

import com.airtribe.task_master.dto.RegisterRequest;
import com.airtribe.task_master.dto.UserDto;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getProfile(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() ->new IllegalArgumentException("User not found"));
        return new UserDto(user);
    }

    @Transactional
    public UserDto updateProfile(String username, String email,
        String firstName, String lastName) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        User updatedUser = userRepository.save(user);
        return new UserDto(updatedUser);
    }
}
