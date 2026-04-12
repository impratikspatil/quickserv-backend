package com.example.kptech.quickserv.controller;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.service.ServiceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private static final String UPLOAD_DIR = "uploads/";


    @Autowired
    private ServiceDetailsService serviceDetailsService;

    // Get services based on query parameters
    @GetMapping
    public ResponseEntity<List<ServiceDetails>> getServicesDetails(
            @RequestParam(required = false) Integer categoryId) {

        List<ServiceDetails> services;

        if (categoryId != null) {
            // If categoryId is provided, filter by category
            services = serviceDetailsService.getServicesByCategory(categoryId);
        }  else {
            // If neither is provided, get all services
            services = serviceDetailsService.getAllServices();
        }

        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get single service by serviceId
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDetails> getServiceById(@PathVariable Integer serviceId) {
        List<ServiceDetails> services = serviceDetailsService.getServicesByServiceId(serviceId);
        if (services.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(services.get(0));
    }


    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Integer serviceId) {
        boolean isDeleted = serviceDetailsService.deleteService(serviceId);
        if (isDeleted) {
            return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/create")
    public ResponseEntity<ServiceDetails> addServiceWithImage(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("whatsappNumber") String whatsappNumber,
            @RequestParam("description") String description,
            @RequestParam("serviceCategory") String serviceCategory,
            @RequestParam("price") Integer price,
            @RequestParam("state") String state,
            @RequestParam("district") String district,
            @RequestParam("pincode") String pincode,
            @RequestParam("address") String address,
            @RequestParam("rateType") String rateType,
            @RequestParam("rating") String rating,
            @RequestParam("location") String location,
            @RequestParam("tags") List<String> tags,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("serviceId") Integer serviceId,
            @RequestParam("isVerified") Boolean isVerified,
            @RequestParam("rateCount") Integer rateCount,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "city", required = false) String city,
            Principal principal


    ) {
        try {

            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = serviceDetailsService.saveImageToLocal(image);
            }

//            String imagePath = serviceDetailsService.saveImageToLocal(image);
            String userEmail = principal.getName();
            ServiceDetails service = new ServiceDetails();
            service.setServiceName(serviceName);
            service.setWhatsappNumber(whatsappNumber);
            service.setDescription(description);
            service.setServiceCategory(serviceCategory);
            service.setPrice(price);
            service.setState(state);
            service.setDistrict(district);
            service.setPincode(pincode);
            service.setAddress(address);
            service.setRateType(rateType);
            service.setRating(rating);
            service.setLocation(location);
            service.setTags(tags);
            service.setCategoryId(categoryId);
            service.setImageUrl(imagePath);
            service.setServiceId(serviceId);
            service.setIsVerified(isVerified);
            service.setRateCount(rateCount);
            service.setPostedBy(userEmail);
            service.setLatitude(latitude);
            service.setLongitude(longitude);
            service.setCity(city);

            ServiceDetails saved = serviceDetailsService.addService(service);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<ServiceDetails>> getFavoriteServices(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = principal.getName();
        // This calls a service method we will create below
        List<ServiceDetails> favorites = serviceDetailsService.getFavoriteServicesForUser(userEmail);

        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    // Get all services posted by the logged-in user
    @GetMapping("/my-services")
    public ResponseEntity<List<ServiceDetails>> getMyServices(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = principal.getName();
        List<ServiceDetails> myServices = serviceDetailsService.getServicesByPostedBy(userEmail);

        return new ResponseEntity<>(myServices, HttpStatus.OK);
    }

    // Update a service (with ownership validation)
    @PutMapping("/update/{serviceId}")
    public ResponseEntity<?> updateService(
            @PathVariable Integer serviceId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("whatsappNumber") String whatsappNumber,
            @RequestParam("description") String description,
            @RequestParam("serviceCategory") String serviceCategory,
            @RequestParam("price") Integer price,
            @RequestParam("state") String state,
            @RequestParam("district") String district,
            @RequestParam("pincode") String pincode,
            @RequestParam("address") String address,
            @RequestParam("rateType") String rateType,
            @RequestParam("location") String location,
            @RequestParam("tags") List<String> tags,
            @RequestParam("categoryId") Integer categoryId,
            Principal principal
    ) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String userEmail = principal.getName();

            // Get existing service
            List<ServiceDetails> existingServices = serviceDetailsService.getServicesByServiceId(serviceId);
            if (existingServices.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
            }

            ServiceDetails existingService = existingServices.get(0);

            // Validate ownership
            if (!existingService.getPostedBy().equals(userEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to edit this service");
            }

            // Handle image update
            String imagePath = existingService.getImageUrl();
            if (image != null && !image.isEmpty()) {
                imagePath = serviceDetailsService.saveImageToLocal(image);
            }

            // Update service fields
            existingService.setServiceName(serviceName);
            existingService.setWhatsappNumber(whatsappNumber);
            existingService.setDescription(description);
            existingService.setServiceCategory(serviceCategory);
            existingService.setPrice(price);
            existingService.setState(state);
            existingService.setDistrict(district);
            existingService.setPincode(pincode);
            existingService.setAddress(address);
            existingService.setRateType(rateType);
            existingService.setLocation(location);
            existingService.setTags(tags);
            existingService.setCategoryId(categoryId);
            existingService.setImageUrl(imagePath);
            existingService.setModifiedAt(new Date());

            ServiceDetails updated = serviceDetailsService.updateService(existingService);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating service: " + e.getMessage());
        }
    }

    // Delete a service (with ownership validation)
    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteServiceByOwner(@PathVariable Integer serviceId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String userEmail = principal.getName();

        // Get existing service
        List<ServiceDetails> existingServices = serviceDetailsService.getServicesByServiceId(serviceId);
        if (existingServices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        ServiceDetails existingService = existingServices.get(0);

        // Validate ownership
        if (!existingService.getPostedBy().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this service");
        }

        boolean isDeleted = serviceDetailsService.deleteService(serviceId);
        if (isDeleted) {
            return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search services by location (city/district/state)
    @GetMapping("/search/location")
    public ResponseEntity<List<ServiceDetails>> searchByLocation(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String state) {
        
        List<ServiceDetails> services = serviceDetailsService.searchByLocation(city, district, state);
        return ResponseEntity.ok(services);
    }

    // Get services near a specific latitude/longitude within a radius (in km)
    @GetMapping("/search/nearby")
    public ResponseEntity<List<ServiceDetails>> searchNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double radiusKm) {
        
        List<ServiceDetails> services = serviceDetailsService.findServicesNearby(latitude, longitude, radiusKm);
        return ResponseEntity.ok(services);
    }



}

