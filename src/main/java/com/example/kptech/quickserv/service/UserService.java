package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<User> getAllUsers() {
        return userDetailsRepository.findAll();
    }

    public User getUserById(Integer userId)
    {
        return userDetailsRepository.findByUserId(userId);
    }

    public User createUser(User user)
    {
        user.setCreatedAt(new Date());
        user.setModifiedAt(new Date());
        return userDetailsRepository.save(user);
    }

}
