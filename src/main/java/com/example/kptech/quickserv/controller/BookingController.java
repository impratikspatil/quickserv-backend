package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.Booking;
import com.example.kptech.quickserv.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new booking
     */
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to create a booking");
        }

        try {
            String userEmail = principal.getName();
            Booking savedBooking = bookingService.createBooking(booking, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the booking");
        }
    }

    /**
     * Get all bookings for current user (customer view)
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = principal.getName();
            List<Booking> bookings = bookingService.getUserBookings(userEmail);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all bookings for services posted by current user (provider view)
     */
    @GetMapping("/provider-bookings")
    public ResponseEntity<?> getProviderBookings(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String providerEmail = principal.getName();
            List<Booking> bookings = bookingService.getProviderBookings(providerEmail);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get bookings for a specific service
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<?> getServiceBookings(
            @PathVariable Integer serviceId, 
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<Booking> bookings = bookingService.getServiceBookings(serviceId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get bookings by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(
            @PathVariable String status, 
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<Booking> bookings = bookingService.getBookingsByStatus(status);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update booking status
     */
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable String bookingId,
            @RequestBody Map<String, String> payload,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to update a booking");
        }

        try {
            String status = payload.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Status is required");
            }

            String userEmail = principal.getName();
            Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status, userEmail);
            return ResponseEntity.ok(updatedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the booking");
        }
    }

    /**
     * Cancel a booking
     */
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(
            @PathVariable String bookingId, 
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to cancel a booking");
        }

        try {
            String userEmail = principal.getName();
            Booking cancelledBooking = bookingService.cancelBooking(bookingId, userEmail);
            return ResponseEntity.ok(cancelledBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while cancelling the booking");
        }
    }

    /**
     * Delete a booking
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(
            @PathVariable String bookingId, 
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to delete a booking");
        }

        try {
            String userEmail = principal.getName();
            bookingService.deleteBooking(bookingId, userEmail);
            return ResponseEntity.ok("Booking deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the booking");
        }
    }

    /**
     * Get booking statistics for current user
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getBookingStatistics(
            @RequestParam(required = false, defaultValue = "false") boolean isProvider,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String email = principal.getName();
            BookingService.BookingStatistics stats = 
                bookingService.getBookingStatistics(email, isProvider);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
