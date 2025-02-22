package com.example.kptech.quickserv.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images") // Collection name in MongoDB
@Getter @Setter
public class Image {
    @Id
    private String id;
    private String imageUrl; // Stores the image URL
    private String imageId;

    // Default constructor (no-arguments)
    public Image() {}

    // Constructor that accepts imageUrl
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
