package com.example.sautiyangu.Service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String fileStorageLocation = System.getProperty("user.home") + "/Downloads";

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location
            Path targetLocation = Paths.get(fileStorageLocation).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public byte[] getFile(String fileName) throws IOException {
        Path filePath = Paths.get(fileStorageLocation).resolve(fileName);
        return Files.readAllBytes(filePath);
    }
}