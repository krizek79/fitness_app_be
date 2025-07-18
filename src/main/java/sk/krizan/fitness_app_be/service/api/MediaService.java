package sk.krizan.fitness_app_be.service.api;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    String uploadFile(MultipartFile file, String folderName);

    void validateImage(MultipartFile file);
}
