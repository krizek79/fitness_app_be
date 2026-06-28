package sk.krizan.fitness_app_be.domain.media;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final Cloudinary cloudinary;

    @Value("${media.image.max-size-mb}")
    private int imageMaxSize;

    @Value("${media.upload.base-path}")
    private String basePath;

    @Value("${media.upload.sub-path:}")
    private String subPath;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        validateImage(file);

        try {
            Map<String, Object> options = Map.of(
                    "resource_type", "auto",
                    "folder", resolveFolder(path)
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed", e);
        }
    }

    @Override
    public void deleteFile(String url) {
        try {
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, Map.of("resource_type", "image"));
        } catch (IOException e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete failed", e);
        }
    }

    private String extractPublicId(String url) {
        String marker = "/upload/";
        int uploadIndex = url.indexOf(marker);
        if (uploadIndex == -1) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Invalid Cloudinary URL");
        }
        String afterUpload = url.substring(uploadIndex + marker.length());
        // strip optional version segment (v1234567890/)
        if (afterUpload.startsWith("v") && afterUpload.indexOf('/') > 1) {
            String version = afterUpload.substring(1, afterUpload.indexOf('/'));
            if (version.chars().allMatch(Character::isDigit)) {
                afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
            }
        }
        int dotIndex = afterUpload.lastIndexOf('.');
        return dotIndex != -1 ? afterUpload.substring(0, dotIndex) : afterUpload;
    }

    private String resolveFolder(String path) {
        if (subPath != null && !subPath.isBlank()) {
            return basePath + "/" + subPath + "/" + path;
        }
        return basePath + "/" + path;
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String contentType = file.getContentType();
        if (!List.of("image/jpeg", "image/png").contains(contentType)) {
            throw new ApplicationException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG and PNG images are allowed");
        }

        long maxSizeInBytes = (long) imageMaxSize * 1024 * 1024;
        if (file.getSize() > maxSizeInBytes) {
            throw new ApplicationException(HttpStatus.PAYLOAD_TOO_LARGE, "File size must be under " + imageMaxSize + " MB");
        }
    }
}
