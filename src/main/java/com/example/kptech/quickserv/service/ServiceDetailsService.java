    package com.example.kptech.quickserv.service;
    import com.example.kptech.quickserv.dao.ServiceCategory;
    import com.example.kptech.quickserv.dao.ServiceDetails;
    import com.example.kptech.quickserv.dao.User;
    import com.example.kptech.quickserv.repository.ServiceCategoryRepository;
    import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
    import com.example.kptech.quickserv.repository.UserDetailsRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.Date;
    import java.util.List;
    import java.util.UUID;
    import static com.example.kptech.quickserv.util.Constants.*;


    @Service
    public class ServiceDetailsService {
        private static final String UPLOAD_DIR = "uploads/";


        @Autowired
        private ServiceDetailsRepository serviceDetailsRepository;

        @Autowired
        private UserDetailsRepository userRepository;

        public List<ServiceDetails> getServicesByCategory(Integer categoryId) {
            return serviceDetailsRepository.findByCategoryId(categoryId);
        }

        public List<ServiceDetails> getAllServices() {
            return serviceDetailsRepository.findAll();
        }

    public List<ServiceDetails> getServiceByServiceId(Integer serviceId) {
        return serviceDetailsRepository.findByServiceId(serviceId);
    }

    public List<ServiceDetails> getServicesByServiceId(Integer serviceId) {
        return getServiceByServiceId(serviceId);
    }

        public String getTestData() {
            return "Working";
        }

        public ServiceDetails addService(ServiceDetails serviceDetails) {
            serviceDetails.setCreatedAt(new Date());
            serviceDetails.setModifiedAt(new Date());
            return serviceDetailsRepository.save(serviceDetails);
        }

        public boolean deleteService(Integer serviceId) {
            if (serviceDetailsRepository.existsByServiceId(serviceId)) {
                serviceDetailsRepository.deleteByServiceId(serviceId); // Delete from MongoDB
                return true;
            } else {
                return false;
            }
        }

        public String saveImageToLocal(MultipartFile image) throws IOException {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get(IMAGE_UPLOAD_DIR + fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());

            // return path to access later (could be a full URL if hosted)
            return BASE_URL+UPLOAD_DIR+fileName;
        }


        public List<ServiceDetails> getFavoriteServicesForUser(String email) {
            // 1. Find the user by email
            User user = userRepository.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            // 2. Get favorite IDs (stored as Strings in your DB: ["5", "6"])
            List<String> favoriteIds = user.getFavoriteServiceIds();

            if (favoriteIds == null || favoriteIds.isEmpty()) {
                return new java.util.ArrayList<>();
            }

            // 3. CONVERT Strings to Integers to match the ServiceDetails field type
            // Inside ServiceDetailsService.java
            List<Integer> favoriteIntIds = favoriteIds.stream()
                    .map(Integer::parseInt) // Crucial: Converts String "5" to Integer 5
                    .toList();

            return serviceDetailsRepository.findByServiceIdIn(favoriteIntIds); //
        }



    }
