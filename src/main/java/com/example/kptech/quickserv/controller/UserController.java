package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userDetailsService;


    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users= userDetailsService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId)
    {
        User user= userDetailsService.getUserById(userId);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User saveduser=userDetailsService.createUser(user);
        return new ResponseEntity<>(saveduser,HttpStatus.CREATED);

    }

    @PostMapping("/{userId}/favorites/toggle")
    public ResponseEntity<User> toggleUserFavorite(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> payload) {

        String serviceId = payload.get("serviceId");
        User updatedUser = userDetailsService.toggleFavorite(userId, serviceId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody User updatedData, Principal principal) {
        try {
            // Use the email from the token to ensure the user is updating their own account
            String userEmail = principal.getName();
            User savedUser = userDetailsService.updateUserByEmail(userEmail, updatedData);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody User user, Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // principal.getName() gets the emailId from your JWT token
            String userEmail = principal.getName();
            User updatedUser = userDetailsService.updateUserByEmail(userEmail, user);

            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            // This catches "User not found" errors from your Service
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // This catches unexpected errors (like database connection issues)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
