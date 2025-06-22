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

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse register(SignupRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmailId(request.getEmailId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setUserId((int) (System.currentTimeMillis() % Integer.MAX_VALUE));

        user.setContactNumber(null);
        user.setLocation(null);

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
}
