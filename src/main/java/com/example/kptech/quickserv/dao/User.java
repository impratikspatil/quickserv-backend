package com.example.kptech.quickserv.dao;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;

@Data
@Document(collection="users")
public class User {

    @Id
    private String id;

    @Field("userId")
    private Integer userId;

    @Field("name")
    private String name;

    @Field("emailId")
    private String emailId;

    @Field("ContactNumber")
    private Long ContactNumber;

    @Field("password")
    private String password;

    @Field("location")
    private String location;

    @Field("role")
    private String role;

    @CreatedDate
    @Field("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Field("modifiedAt")
    private Date modifiedAt;


}
