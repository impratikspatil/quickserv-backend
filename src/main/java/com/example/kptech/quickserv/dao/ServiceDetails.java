package com.example.kptech.quickserv.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;
import java.util.List;

@Data
@Document(collection="service_details")

public class ServiceDetails {

    @Id
    private String id;

    @Field("serviceId")
    private Integer serviceId;

    @Field("postedBy")
    private String postedBy;

    @Field("whatsappNumber")
    private String whatsappNumber;

    @Field("serviceName")
    private String serviceName;

    @Field("description")
    private String description;

    @Field("serviceCategory")
    private String serviceCategory;

    @Field("price")
    private Integer price;

    @Field("state")
    private String state;

    @Field("district")
    private String district;

    @Field("pincode")
    private String pincode;

    @Field("address")
    private String address;

    @Field("rateType")
    private String rateType;

    @Field("rating")
    private String rating;

    @Field("location")
    private String location;

    @Field("tags")
    private List<String> tags;

    @Field("categoryId")
    private Integer categoryId;

    @CreatedDate
    @Field("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Field("modifiedAt")
    private Date modifiedAt;

    @Field("imageUrl")
    private String imageUrl;

    @Field("rateCount")
    private Integer rateCount;

    @Field("isVerified")
    private Boolean isVerified;

    // Additional fields for enhanced functionality
    @Field("openTime")
    private String openTime; // e.g., "8:00 AM"

    @Field("closeTime")
    private String closeTime; // e.g., "7:00 PM"

    @Field("workingDays")
    private List<String> workingDays; // e.g., ["Monday", "Tuesday", "Wednesday"]

    @Field("features")
    private List<String> features; // e.g., ["Air Conditioned", "Parking Available"]

    @Field("established")
    private String established; // e.g., "2020" or "Since 2020"

    @Field("instagram")
    private String instagram;

    @Field("facebook")
    private String facebook;

    @Field("website")
    private String website;

    @Field("galleryImages")
    private List<String> galleryImages; // Additional images beyond the main imageUrl




}

