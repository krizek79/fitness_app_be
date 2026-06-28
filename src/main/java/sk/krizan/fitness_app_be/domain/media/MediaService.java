package sk.krizan.fitness_app_be.domain.media;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    String uploadFile(MultipartFile file, String path);

    void deleteFile(String url);

}
