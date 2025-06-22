package com.example.kptech.quickserv.dto;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String emailId;
    private String password;
    private Long contactNumber;
    private String location;
}
