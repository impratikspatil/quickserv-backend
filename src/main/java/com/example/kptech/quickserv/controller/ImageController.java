package com.example.kptech.quickserv.controller;

import com.example.kptech.quickserv.dao.Image;
import com.example.kptech.quickserv.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ImageService imageService;

    // ✅ Upload Image & Store URL in MongoDB
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Ensure the uploads folder exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Create a unique file name
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = generateUniqueFileName(originalFileName);

            // Save file locally
            String filePath = UPLOAD_DIR + uniqueFileName;
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            // Generate the Image URL (assuming running on localhost)
            String imageUrl = "http://localhost:8080/" + filePath;

            // Save URL in MongoDB
            Image image = imageService.saveImageUrl(imageUrl);

            return ResponseEntity.ok("Image URL stored: " + image.getImageUrl());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error uploading image: " + e.getMessage());
        }
    }

    // ✅ Retrieve Image URL by ID
    @GetMapping("/{imageId}")
    public ResponseEntity<String> getImage(@PathVariable String imageId) {
        Image image = imageService.getImageById(imageId);
        if (image == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(image.getImageUrl());
    }

    // Utility method to generate unique file name based on timestamp
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timestamp + "_" + originalFileName;
    }
}
