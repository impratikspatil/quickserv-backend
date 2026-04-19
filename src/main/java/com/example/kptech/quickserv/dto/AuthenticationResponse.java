package com.example.kptech.quickserv.dto;

import com.example.kptech.quickserv.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private User user;
}
