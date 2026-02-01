package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.Review;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.repository.ReviewRepository;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ServiceDetailsRepository serviceDetailsRepository;

    @Autowired
    private UserDetailsRepository userRepository;

    /**
     * Create a new review
     */
    public Review createReview(Review review, String userEmail) {
        // Get user details
        User user = userRepository.findByEmailId(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already reviewed this service
        if (reviewRepository.existsByServiceIdAndUserId(review.getServiceId(), user.getUserId())) {
            throw new RuntimeException("You have already reviewed this service");
        }

        // Set user information
        review.setUserId(user.getUserId());
        review.setUserEmail(userEmail);
        review.setUserName(user.getName());
        review.setCreatedAt(new Date());

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Save review
        Review savedReview = reviewRepository.save(review);

        // Update service rating
        updateServiceRating(review.getServiceId());

        return savedReview;
    }

    /**
     * Get all reviews for a service
     */
    public List<Review> getServiceReviews(Integer serviceId) {
        return reviewRepository.findByServiceIdOrderByCreatedAtDesc(serviceId);
    }

    /**
     * Get reviews by user
     */
    public List<Review> getUserReviews(String userEmail) {
        return reviewRepository.findByUserEmail(userEmail);
    }

    /**
     * Mark review as helpful
     */
    public Review markReviewHelpful(String reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setHelpful(review.getHelpful() + 1);
        return reviewRepository.save(review);
    }

    /**
     * Delete a review
     */
    public void deleteReview(String reviewId, String userEmail) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Check if user owns this review
        if (!review.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);

        // Update service rating after deletion
        updateServiceRating(review.getServiceId());
    }

    /**
     * Update service's average rating and review count
     */
    private void updateServiceRating(Integer serviceId) {
        List<ServiceDetails> services = serviceDetailsRepository.findByServiceId(serviceId);
        if (services.isEmpty()) {
            return;
        }

        ServiceDetails service = services.get(0);

        // Get all reviews for this service
        List<Review> reviews = reviewRepository.findByServiceId(serviceId);

        if (reviews.isEmpty()) {
            service.setRating("0");
            service.setRateCount(0);
        } else {
            // Calculate average rating
            double averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);

            service.setRating(String.format("%.1f", averageRating));
            service.setRateCount(reviews.size());
        }

        serviceDetailsRepository.save(service);
    }

    /**
     * Get review statistics for a service
     */
    public ReviewStatistics getReviewStatistics(Integer serviceId) {
        List<Review> reviews = reviewRepository.findByServiceId(serviceId);

        ReviewStatistics stats = new ReviewStatistics();
        stats.setTotalReviews(reviews.size());

        if (!reviews.isEmpty()) {
            double average = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            stats.setAverageRating(average);

            // Count ratings by star
            long fiveStars = reviews.stream().filter(r -> r.getRating() == 5).count();
            long fourStars = reviews.stream().filter(r -> r.getRating() == 4).count();
            long threeStars = reviews.stream().filter(r -> r.getRating() == 3).count();
            long twoStars = reviews.stream().filter(r -> r.getRating() == 2).count();
            long oneStar = reviews.stream().filter(r -> r.getRating() == 1).count();

            stats.setFiveStars((int) fiveStars);
            stats.setFourStars((int) fourStars);
            stats.setThreeStars((int) threeStars);
            stats.setTwoStars((int) twoStars);
            stats.setOneStar((int) oneStar);
        }

        return stats;
    }

    // Inner class for review statistics
    public static class ReviewStatistics {
        private int totalReviews;
        private double averageRating;
        private int fiveStars;
        private int fourStars;
        private int threeStars;
        private int twoStars;
        private int oneStar;

        // Getters and setters
        public int getTotalReviews() { return totalReviews; }
        public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

        public int getFiveStars() { return fiveStars; }
        public void setFiveStars(int fiveStars) { this.fiveStars = fiveStars; }

        public int getFourStars() { return fourStars; }
        public void setFourStars(int fourStars) { this.fourStars = fourStars; }

        public int getThreeStars() { return threeStars; }
        public void setThreeStars(int threeStars) { this.threeStars = threeStars; }

        public int getTwoStars() { return twoStars; }
        public void setTwoStars(int twoStars) { this.twoStars = twoStars; }

        public int getOneStar() { return oneStar; }
        public void setOneStar(int oneStar) { this.oneStar = oneStar; }
    }
}
