package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<User> getAllUsers() {
        return userDetailsRepository.findAll();
    }

    public User getUserById(Integer userId)
    {
        return userDetailsRepository.findByUserId(userId);
    }

    public User createUser(User user)
    {
        user.setCreatedAt(new Date());
        user.setModifiedAt(new Date());
        return userDetailsRepository.save(user);
    }

    public User toggleFavorite(Integer userId, String serviceId) {
        // Fetch the user using your existing repository method
        User user = userDetailsRepository.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Initialize the list if it's null (first time favoriting)
        if (user.getFavoriteServiceIds() == null) {
            user.setFavoriteServiceIds(new java.util.ArrayList<>());
        }

        List<String> favorites = user.getFavoriteServiceIds();

        if (favorites.contains(serviceId)) {
            // Remove if already favorited
            favorites.remove(serviceId);
        } else {
            // Add if not favorited
            favorites.add(serviceId);
        }

        user.setModifiedAt(new Date());
        return userDetailsRepository.save(user);
    }

    public User updateUser(User updatedData) {
        // 1. Find existing user
        User existingUser = userDetailsRepository.findByEmailId(updatedData.getEmailId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Update fields
        existingUser.setName(updatedData.getName());
        existingUser.setContactNumber(updatedData.getContactNumber());
        existingUser.setLocation(updatedData.getLocation());
        existingUser.setModifiedAt(new Date());

        // 3. Save back to MongoDB
        return userDetailsRepository.save(existingUser);
    }

    public User updateUserByEmail(String email, User updatedData) {
        // 1. Find the user by email (from the JWT Principal)
        User existingUser = userDetailsRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 2. Map the fields from the React request to the Database object
        existingUser.setName(updatedData.getName());
        existingUser.setContactNumber(updatedData.getContactNumber());
        existingUser.setLocation(updatedData.getLocation());

        // Handle the profile image (Base64 string)
        if (updatedData.getProfileImage() != null) {
            existingUser.setProfileImage(updatedData.getProfileImage());
        }

        existingUser.setModifiedAt(new Date());

        // 3. Save the updated document back to MongoDB
        return userDetailsRepository.save(existingUser);
    }
}
