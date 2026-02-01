package com.example.kptech.quickserv.repository;

import com.example.kptech.quickserv.dao.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    
    List<Review> findByServiceId(Integer serviceId);
    
    List<Review> findByUserId(String userId);
    
    List<Review> findByServiceIdOrderByCreatedAtDesc(Integer serviceId);
    
    List<Review> findByUserEmail(String userEmail);
    
    Optional<Review> findByReviewId(String reviewId);
    
    // Check if user already reviewed a service
    boolean existsByServiceIdAndUserId(Integer serviceId, String userId);
    
    // Count reviews for a service
    Long countByServiceId(Integer serviceId);
    
    // Get average rating for a service
    @Query("{ 'serviceId': ?0 }")
    List<Review> findReviewsForAverageCalculation(Integer serviceId);
}
