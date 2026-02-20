package com.example.lms_evaluation_service.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path path;

    @PostConstruct
    public void init() throws IOException {

        path = Paths.get(uploadDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {

        try {

            String fileName =
                    UUID.randomUUID() + "_" +
                            file.getOriginalFilename();

            Path filePath = path.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    @Override
    public Resource downloadFile(String fileName) {

        Path filePath = path.resolve(fileName);

        return new PathResource(filePath);
    }
}
