package com.example.kptech.quickserv.repository;

import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailsRepository extends MongoRepository<User, String> {

    User findByUserId(Integer userId);

    Optional<User> findByEmailId(String emailId);


    boolean existsByEmailId(String emailId);
}
