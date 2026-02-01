package com.example.kptech.quickserv.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;
import java.util.UUID;

@Data
@Document(collection="bookings")
public class Booking {
    
    @Id
    private String id;
    
    @Field("bookingId")
    private String bookingId;
    
    @Field("serviceId")
    private Integer serviceId;
    
    @Field("serviceName")
    private String serviceName;
    
    @Field("userId")
    private String userId;
    
    @Field("userEmail")
    private String userEmail;
    
    @Field("userName")
    private String userName;
    
    @Field("userPhone")
    private String userPhone;
    
    @Field("preferredDate")
    private Date preferredDate;
    
    @Field("preferredTime")
    private String preferredTime;
    
    @Field("message")
    private String message;
    
    @Field("status")
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    
    @CreatedDate
    @Field("createdAt")
    private Date createdAt;
    
    @Field("updatedAt")
    private Date updatedAt;
    
    @Field("providerEmail")
    private String providerEmail; // Service provider's email
    
    @Field("providerContact")
    private String providerContact; // Service provider's contact
    
    public Booking() {
        this.bookingId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "PENDING";
    }
}
