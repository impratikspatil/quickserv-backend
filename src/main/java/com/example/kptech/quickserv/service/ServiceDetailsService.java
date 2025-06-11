package com.example.kptech.quickserv.service;
import com.example.kptech.quickserv.dao.ServiceCategory;
import com.example.kptech.quickserv.dao.ServiceDetails;
import com.example.kptech.quickserv.repository.ServiceCategoryRepository;
import com.example.kptech.quickserv.repository.ServiceDetailsRepository;
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


    public List<ServiceDetails> getServicesByCategory(Integer categoryId) {
        return serviceDetailsRepository.findByCategoryId(categoryId);
    }

    public List<ServiceDetails> getAllServices() {
        return serviceDetailsRepository.findAll();
    }

    public List<ServiceDetails> getServiceByServiceId(Integer serviceId) {
        return serviceDetailsRepository.findByServiceId(serviceId);
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








}
