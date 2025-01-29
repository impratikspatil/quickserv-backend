package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.service.CategoryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryDetailsService categoryDetailsService;

    @GetMapping()
    public ResponseEntity<List<ServiceCategory>> getCategoryDetails(

            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String categoryName

    ) {
        List<ServiceCategory> categories;

        if (categoryId != null) {
            categories=categoryDetailsService.getCategoryDetailsById(categoryId);
        }
        else if(categoryName!=null) {
            categories = categoryDetailsService.getCategoryDetailsByName(categoryName);

        }
        else {
            categories = categoryDetailsService.getAllCategories();
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
