package com.example.kptech.quickserv.controller;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.service.ServiceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceDetailsService serviceDetailsService;

    // Get services based on query parameters
    @GetMapping
    public ResponseEntity<List<ServiceDetails>> getServicesDetails(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer serviceId) {

        List<ServiceDetails> services;

        if (categoryId != null) {
            // If categoryId is provided, filter by category
            services = serviceDetailsService.getServicesByCategory(categoryId);
        } else if (serviceId != null) {
            // If serviceId is provided, filter by serviceId
            services = serviceDetailsService.getServiceByServiceId(serviceId);
        } else {
            // If neither is provided, get all services
            services = serviceDetailsService.getAllServices();
        }

        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceDetails> addService(@RequestBody ServiceDetails serviceDetails) {
        ServiceDetails newService = serviceDetailsService.addService(serviceDetails);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Integer serviceId) {
        boolean isDeleted = serviceDetailsService.deleteService(serviceId);
        if (isDeleted) {
            return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
        }
    }
}

