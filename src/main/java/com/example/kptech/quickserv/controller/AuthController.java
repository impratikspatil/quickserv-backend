package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dto.LoginRequest;
import com.example.kptech.quickserv.dto.SignupRequest;
import com.example.kptech.quickserv.dto.AuthenticationResponse;
import com.example.kptech.quickserv.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/signup")
    public AuthenticationResponse signup(@RequestBody SignupRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/google")
    public AuthenticationResponse googleLogin(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        return authService.googleLogin(token);
    }

}
