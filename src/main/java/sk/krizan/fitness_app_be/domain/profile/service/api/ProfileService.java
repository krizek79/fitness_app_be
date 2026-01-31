package sk.krizan.fitness_app_be.domain.profile.service.api;

import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public interface ProfileService {

    PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request);

    Profile getProfileById(Long id);

    Long deleteProfile(Long id);

    String uploadProfilePicture(MultipartFile multipartFile);
}
