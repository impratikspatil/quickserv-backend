package com.example.kptech.quickserv.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String emailId;
    private String password;
}
