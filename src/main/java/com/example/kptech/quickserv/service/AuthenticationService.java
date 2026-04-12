package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.dto.AuthenticationResponse;
import com.example.kptech.quickserv.dto.LoginRequest;
import com.example.kptech.quickserv.dto.SignupRequest;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import com.example.kptech.quickserv.util.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;



@Service
public class AuthenticationService {

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse register(SignupRequest request) {
        if (request.getName() == null || request.getEmailId() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Name, email, and password are required.");
        }

        if (userRepo.findByEmailId(request.getEmailId()).isPresent()) {
            throw new RuntimeException("User already exists with this email!");
        }

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmailId(request.getEmailId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setContactNumber(null);
        user.setLocation(null);
        user.setRole("USER");

        userRepo.save(user);

        String token = jwtService.generateToken(user.getEmailId());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepo.findByEmailId(request.getEmailId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmailId());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse googleLogin(String googleToken) {
        try {


            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    jsonFactory
            )
                    .setAudience(Collections.singletonList("888172117082-v2d5t74i5vmfbcib7e69ledhlhivalhd.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            Optional<User> userOpt = userRepo.findByEmailId(email);
            User user = userOpt.orElse(null);

            if (user == null) {
                user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setEmailId(email);
                user.setName(name);
                user.setRole("USER");
                userRepo.save(user);
            }

            String jwt = jwtService.generateToken(user.getEmailId());

            return new AuthenticationResponse(jwt);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google login failed");
        }
    }

}
