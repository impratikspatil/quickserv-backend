package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.ServiceCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping
public class GlobalController {

    @GetMapping("/")
    public String home() {
        return "Welcome to QuickServ API!";
    }
}
