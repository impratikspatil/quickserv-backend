package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.Review;
import com.example.kptech.quickserv.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Get all reviews for a specific service
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<Review>> getServiceReviews(@PathVariable Integer serviceId) {
        try {
            List<Review> reviews = reviewService.getServiceReviews(serviceId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get review statistics for a service
     */
    @GetMapping("/service/{serviceId}/statistics")
    public ResponseEntity<ReviewService.ReviewStatistics> getReviewStatistics(@PathVariable Integer serviceId) {
        try {
            ReviewService.ReviewStatistics stats = reviewService.getReviewStatistics(serviceId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all reviews by current user
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<List<Review>> getMyReviews(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = principal.getName();
            List<Review> reviews = reviewService.getUserReviews(userEmail);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new review
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody Review review, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to submit a review");
        }

        try {
            String userEmail = principal.getName();
            Review savedReview = reviewService.createReview(review, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the review");
        }
    }

    /**
     * Mark a review as helpful
     */
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<?> markReviewHelpful(@PathVariable String reviewId) {
        try {
            Review updatedReview = reviewService.markReviewHelpful(reviewId);
            return ResponseEntity.ok(updatedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the review");
        }
    }

    /**
     * Delete a review
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to delete a review");
        }

        try {
            String userEmail = principal.getName();
            reviewService.deleteReview(reviewId, userEmail);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the review");
        }
    }
}
