package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /*
     Spring automatically creates an object (bean) of UserRepository
     implementation at application startup because it extends JpaRepository.

     The @Autowired annotation tells Spring to inject that repository bean
     into this service class, so we can directly use database operations
     without manually creating repository objects.
    */

    // Register a new user
    public User registerUser(@NonNull User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    // Find user using UPI ID
    public Optional<User> findByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId);
    }

    // Find users with balance greater than <amount>
    public List<User> findUsersWithBalanceGreaterThan(Double amount) {
        return userRepository.findUsersWithBalanceGreaterThan(amount);
    }
}
