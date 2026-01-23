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

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Principal.getName() typically returns the email/username from your JWT
        String userEmail = principal.getName();
        User user = userDetailsService.getUserByEmail(userEmail);
        return ResponseEntity.ok(user);
    }


    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users= userDetailsService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId:[0-9]+}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userDetailsService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User saveduser=userDetailsService.createUser(user);
        return new ResponseEntity<>(saveduser,HttpStatus.CREATED);

    }

    @PostMapping("/favorites/toggle") // Removed {userId} from path
    public ResponseEntity<?> toggleUserFavorite(
            Principal principal,
            @RequestBody Map<String, String> payload) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            String userEmail = principal.getName(); // Gets "impratikspatil@gmail.com"
            String serviceId = payload.get("serviceId");

            // We need to update this method in UserService too
            User updatedUser = userDetailsService.toggleFavoriteByEmail(userEmail, serviceId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE the old updateUserInfo and updateProfile methods and replace with this:
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody User user, Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            // This calls the service method we wrote with the profileImage logic
            String userEmail = principal.getName();
            User updatedUser = userDetailsService.updateUserByEmail(userEmail, user);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed: " + e.getMessage());
        }
    }
}
