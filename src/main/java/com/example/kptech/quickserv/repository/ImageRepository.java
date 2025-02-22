package com.example.kptech.quickserv.repository;

import com.example.kptech.quickserv.dao.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {

    Image findByImageId(String imageId);

}
