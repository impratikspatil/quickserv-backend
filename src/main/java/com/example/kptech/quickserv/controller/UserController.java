package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.service.ServiceDetailsService;
import com.example.kptech.quickserv.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;


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


}
