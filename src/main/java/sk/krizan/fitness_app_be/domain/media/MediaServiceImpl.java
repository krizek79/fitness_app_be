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

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        validateImage(file);

        try {
            Map<String, Object> options = Map.of(
                    "resource_type", "auto",
                    "folder", folderName
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed", e);
        }
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
