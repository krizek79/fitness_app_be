package sk.krizan.fitness_app_be.domain.profile.service.api;

import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public interface ProfileService {

    PageResponse<ProfileDetailResponse> filterProfiles(ProfileFilterRequest request);

    Profile getProfileById(Long id);

    void deleteProfile(Long id);

    String uploadProfilePicture(MultipartFile multipartFile);

}
