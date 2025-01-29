package com.example.kptech.quickserv.service;
import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.repository.ServiceCategoryRepository;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDetailsService {
    @Autowired
    private ServiceDetailsRepository serviceDetailsRepository;


    public List<ServiceDetails> getServicesByCategory(Integer categoryId) {
        return serviceDetailsRepository.findByCategoryId(categoryId);
    }

    public List<ServiceDetails> getAllServices() {
        return serviceDetailsRepository.findAll();
    }

    public List<ServiceDetails> getServiceByServiceId(Integer serviceId) {
        return serviceDetailsRepository.findByServiceId(serviceId);
    }

    public String getTestData() {
        return "Working";
    }

    public ServiceDetails addService(ServiceDetails serviceDetails) {
        return serviceDetailsRepository.save(serviceDetails);
    }

    public boolean deleteService(Integer serviceId) {
        if (serviceDetailsRepository.existsByServiceId(serviceId)) {
            serviceDetailsRepository.deleteByServiceId(serviceId); // Delete from MongoDB
            return true;
        } else {
            return false;
        }
    }






}
