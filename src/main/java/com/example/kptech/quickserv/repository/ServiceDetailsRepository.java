package com.example.kptech.quickserv.repository;

import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.dao.ServiceDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDetailsRepository extends MongoRepository<ServiceDetails, String> {

     List<ServiceDetails> findByCategoryId(Integer categoryId);
    List<ServiceDetails> findByServiceId(Integer serviceId);
    void deleteByServiceId(Integer serviceId);
    boolean existsByServiceId(Integer serviceId);
    List<ServiceDetails> findByServiceIdIn(List<Integer> serviceIds);


}

