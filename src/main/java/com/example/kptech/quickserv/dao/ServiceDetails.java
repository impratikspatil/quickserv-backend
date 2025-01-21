package com.example.kptech.quickserv.dao;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection="service_details")
public class ServiceDetails {

    @Id
    private String id;


    @Field("company_id")
    private String companyId;

    @Field("company_name")
    private String companyName;

    @Field("category_id")
    private String categoryId;

    @Field("address")
    private String address;
//    private Long phone_no;
//    private List<String> assets;
//    private Integer ratings;
}

