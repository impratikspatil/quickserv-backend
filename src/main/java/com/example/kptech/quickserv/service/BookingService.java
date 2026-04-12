package com.example.kptech.quickserv.service;

import com.example.kptech.quickserv.dao.Booking;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.dao.User;
import com.example.kptech.quickserv.repository.BookingRepository;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
import com.example.kptech.quickserv.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceDetailsRepository serviceDetailsRepository;

    @Autowired
    private UserDetailsRepository userRepository;

    /**
     * Create a new booking
     */
    public Booking createBooking(Booking booking, String userEmail) {
        // Get user details
        User user = userRepository.findByEmailId(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get service details
        List<ServiceDetails> services = serviceDetailsRepository.findByServiceId(booking.getServiceId());
        if (services.isEmpty()) {
            throw new RuntimeException("Service not found");
        }
        ServiceDetails service = services.get(0);

        // Set booking information
        booking.setUserId(user.getUserId());
        booking.setUserEmail(userEmail);
        booking.setUserName(user.getName());
        booking.setServiceName(service.getServiceName());
        booking.setProviderEmail(service.getPostedBy());
        booking.setProviderContact(service.getWhatsappNumber());
        booking.setCreatedAt(new Date());
        booking.setUpdatedAt(new Date());
        booking.setStatus("PENDING");

        // Validate required fields
        if (booking.getUserPhone() == null || booking.getUserPhone().isEmpty()) {
            throw new RuntimeException("Phone number is required");
        }
        if (booking.getPreferredDate() == null) {
            throw new RuntimeException("Preferred date is required");
        }

        // Save booking
        return bookingRepository.save(booking);
    }

    /**
     * Get all bookings by user
     */
    public List<Booking> getUserBookings(String userEmail) {
        return bookingRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    /**
     * Get all bookings for a service provider
     */
    public List<Booking> getProviderBookings(String providerEmail) {
        return bookingRepository.findByProviderEmailOrderByCreatedAtDesc(providerEmail);
    }

    /**
     * Get bookings by service
     */
    public List<Booking> getServiceBookings(Integer serviceId) {
        return bookingRepository.findByServiceId(serviceId);
    }

    /**
     * Get bookings by status
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    /**
     * Update booking status
     */
    public Booking updateBookingStatus(String bookingId, String status, String userEmail) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user is the provider or the customer
        if (!booking.getProviderEmail().equals(userEmail) && 
            !booking.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this booking");
        }

        // Validate status
        if (!isValidStatus(status)) {
            throw new RuntimeException("Invalid status. Use: PENDING, CONFIRMED, COMPLETED, or CANCELLED");
        }

        booking.setStatus(status);
        booking.setUpdatedAt(new Date());

        return bookingRepository.save(booking);
    }

    /**
     * Cancel a booking
     */
    public Booking cancelBooking(String bookingId, String userEmail) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking
        if (!booking.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        // Check if booking can be cancelled
        if (booking.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Cannot cancel a completed booking");
        }

        booking.setStatus("CANCELLED");
        booking.setUpdatedAt(new Date());

        return bookingRepository.save(booking);
    }

    /**
     * Delete a booking
     */
    public void deleteBooking(String bookingId, String userEmail) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Only allow deletion by the user who created it or the provider
        if (!booking.getUserEmail().equals(userEmail) && 
            !booking.getProviderEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to delete this booking");
        }

        bookingRepository.delete(booking);
    }

    /**
     * Get booking statistics
     */
    public BookingStatistics getBookingStatistics(String email, boolean isProvider) {
        List<Booking> bookings = isProvider 
            ? bookingRepository.findByProviderEmail(email)
            : bookingRepository.findByUserEmail(email);

        BookingStatistics stats = new BookingStatistics();
        stats.setTotalBookings(bookings.size());

        long pending = bookings.stream().filter(b -> b.getStatus().equals("PENDING")).count();
        long confirmed = bookings.stream().filter(b -> b.getStatus().equals("CONFIRMED")).count();
        long completed = bookings.stream().filter(b -> b.getStatus().equals("COMPLETED")).count();
        long cancelled = bookings.stream().filter(b -> b.getStatus().equals("CANCELLED")).count();

        stats.setPendingBookings((int) pending);
        stats.setConfirmedBookings((int) confirmed);
        stats.setCompletedBookings((int) completed);
        stats.setCancelledBookings((int) cancelled);

        return stats;
    }

    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || status.equals("CONFIRMED") || 
               status.equals("COMPLETED") || status.equals("CANCELLED");
    }

    // Inner class for booking statistics
    public static class BookingStatistics {
        private int totalBookings;
        private int pendingBookings;
        private int confirmedBookings;
        private int completedBookings;
        private int cancelledBookings;

        // Getters and setters
        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

        public int getPendingBookings() { return pendingBookings; }
        public void setPendingBookings(int pendingBookings) { this.pendingBookings = pendingBookings; }

        public int getConfirmedBookings() { return confirmedBookings; }
        public void setConfirmedBookings(int confirmedBookings) { this.confirmedBookings = confirmedBookings; }

        public int getCompletedBookings() { return completedBookings; }
        public void setCompletedBookings(int completedBookings) { this.completedBookings = completedBookings; }

        public int getCancelledBookings() { return cancelledBookings; }
        public void setCancelledBookings(int cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    }
}
