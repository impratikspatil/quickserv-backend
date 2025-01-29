package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<User> getAllUsers() {
        return userDetailsRepository.findAll();
    }

    public User getUserById(Integer userId)
    {
        return userDetailsRepository.findByUserId(userId);
    }

}
