package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.Image;
import com.example.kptech.quickserv.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    // Save image URL to MongoDB
    public Image saveImageUrl(String imageUrl) {
        return imageRepository.save(new Image(imageUrl));
    }

    // Retrieve image URL by ID
    public Image getImageById(String imageId) {
        return imageRepository.findByImageId(imageId);
    }
}
