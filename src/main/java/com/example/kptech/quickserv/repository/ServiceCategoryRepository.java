package com.example.kptech.quickserv.repository;


import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.dao.ServiceDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServiceCategoryRepository extends MongoRepository<ServiceCategory, String> {

    List<ServiceCategory> findByCategoryId(Integer categoryId);

    List<ServiceCategory> findByCategoryName(String categoryName);

    void deleteByCategoryId(Integer CategoryId);
    boolean existsByCategoryId(Integer CategoryId);



}
