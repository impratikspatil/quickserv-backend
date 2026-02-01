package com.example.kptech.quickserv.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;
import java.util.UUID;

@Data
@Document(collection="reviews")
public class Review {
    
    @Id
    private String id;
    
    @Field("reviewId")
    private String reviewId;
    
    @Field("serviceId")
    private Integer serviceId;
    
    @Field("userId")
    private String userId;
    
    @Field("userEmail")
    private String userEmail;
    
    @Field("userName")
    private String userName;
    
    @Field("rating")
    private Integer rating; // 1-5 stars
    
    @Field("comment")
    private String comment;
    
    @CreatedDate
    @Field("createdAt")
    private Date createdAt;
    
    @Field("helpful")
    private Integer helpful; // Count of people who found it helpful
    
    @Field("verified")
    private Boolean verified; // Verified purchase/booking
    
    public Review() {
        this.reviewId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.helpful = 0;
        this.verified = false;
    }
}
