package sk.krizan.fitness_app_be.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.service.api.MediaService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final Cloudinary cloudinary;

    @Value("${media.image.max-size-mb}")
    private int imageMaxSize;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            Map<String, Object> options = Map.of(
                    "resource_type", "auto",
                    "folder", folderName
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalOperationException("File is empty");
        }

        String contentType = file.getContentType();
        if (!List.of("image/jpeg", "image/png").contains(contentType)) {
            throw new IllegalOperationException("Only JPEG and PNG images are allowed");
        }

        long maxSizeInBytes = 2 * 1024 * 1024;
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalOperationException("File size must be under 2MB");
        }
    }
}
