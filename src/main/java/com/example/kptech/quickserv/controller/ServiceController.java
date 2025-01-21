package com.example.kptech.quickserv.controller;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceDetailsRepository serviceDetailsRepository;

    @GetMapping
    public ResponseEntity<List<ServiceDetails>> getAllServices() {
        List<ServiceDetails> services = serviceDetailsRepository.findAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }


}
