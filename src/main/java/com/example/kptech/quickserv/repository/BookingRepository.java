package com.example.kptech.quickserv.repository;

import com.example.kptech.quickserv.dao.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    
    List<Booking> findByUserId(String userId);
    
    List<Booking> findByUserEmail(String userEmail);
    
    List<Booking> findByServiceId(Integer serviceId);
    
    List<Booking> findByProviderEmail(String providerEmail);
    
    List<Booking> findByStatus(String status);
    
    Optional<Booking> findByBookingId(String bookingId);
    
    List<Booking> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    
    List<Booking> findByProviderEmailOrderByCreatedAtDesc(String providerEmail);
    
    List<Booking> findByServiceIdAndStatus(Integer serviceId, String status);
    
    List<Booking> findByPreferredDateBetween(Date startDate, Date endDate);
    
    Long countByServiceId(Integer serviceId);
    
    Long countByProviderEmail(String providerEmail);
}
