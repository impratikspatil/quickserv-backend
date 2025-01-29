package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryDetailsService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryRepository.findAll();
    }

    public List<ServiceCategory> getCategoryDetailsById(Integer categoryId) {
        return serviceCategoryRepository.findByCategoryId(categoryId);
    }

    public List<ServiceCategory> getCategoryDetailsByName(String categoryName) {
        return serviceCategoryRepository.findByCategoryName(categoryName);
    }



}
