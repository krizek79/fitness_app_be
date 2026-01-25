package sk.krizan.fitness_app_be.service.api;

import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;

public interface ProfileService {

    PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request);

    Profile getProfileById(Long id);

    Long deleteProfile(Long id);

    String uploadProfilePicture(MultipartFile multipartFile);
}
