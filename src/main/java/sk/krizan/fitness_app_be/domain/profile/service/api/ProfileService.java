package sk.krizan.fitness_app_be.domain.profile.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;

public interface ProfileService {

    PageResponse<ProfileDetailResponse> filterProfiles(ProfileFilterRequest request);

    Profile getProfileById(Long id);

    Profile getProfileByPublicId(String publicId);

}
