package com.example.kptech.quickserv.dao;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;

@Data
@Document(collection="service_details")
public class ServiceDetails {

    @Id
    private String id;

    @Field("serviceId")
    private Integer serviceId;

    @Field("serviceName")
    private String serviceName;

    @Field("description")
    private String description;

    @Field("categoryId")
    private Integer categoryId;

    @Field("price")
    private Integer price;

    @Field("rating")
    private Double rating;

    @Field("location")
    private String location;

    @Field("tags")
    private String tags;

    @Field("price_type")
    private String price_type;

    @CreatedDate
    @Field("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Field("modifiedAt")
    private Date modifiedAt;



}

