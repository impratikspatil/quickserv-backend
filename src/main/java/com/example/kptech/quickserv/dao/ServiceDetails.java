package com.example.kptech.quickserv.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
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




}

