package com.example.lms_assignment_service.service;

import com.example.lms_assignment_service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath;

    public LocalFileStorageService(
            @Value("${file.upload-dir}") String uploadDir) throws Exception {

        this.uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(uploadPath);
    }

    @Override
    public String storeFile(MultipartFile file) {

        try {
            long MAX_FILE_SIZE = 8 * 1024 * 1024; // 5MB

            if(file.getSize() > MAX_FILE_SIZE){
                throw new BadRequestException("File size exceeds limit");
            }


            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            String contentType = file.getContentType();

            if(contentType == null ||
                    !(contentType.equals("application/pdf") ||
                            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

                throw new BadRequestException("Only PDF and DOCX allowed");
            }

            Path targetLocation =
                    uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation);

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }
    }

    @Override
    public Resource downloadFile(String fileName) {

        try {

            Path filePath = uploadPath.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            }

            throw new RuntimeException("File not found");

        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }
}
