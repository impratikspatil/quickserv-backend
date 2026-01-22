    package com.example.kptech.quickserv.dao;


    import lombok.Data;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;
    import org.springframework.data.mongodb.core.mapping.Field;
    import java.util.Date;
    import java.util.List;

    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.annotation.CreatedDate;

    @Data
    @Document(collection="users")
    public class User {

        @Id
        private String id;

        @Field("userId")
        private String userId;

        @Field("name")
        private String name;

        @Field("emailId")
        private String emailId;

        @Field("contactNumber")
        private Long contactNumber;

        @Field("favoriteServiceIds")
        private List<String> favoriteServiceIds = new java.util.ArrayList<>();

        @Field("password")
        private String password;

        @Field("location")
        private String location;

        @Field("role")
        private String role;

        @Field("profileImage")
        private String profileImage;

        @CreatedDate
        @Field("createdAt")
        private Date createdAt;

        @LastModifiedDate
        @Field("modifiedAt")
        private Date modifiedAt;




    }
