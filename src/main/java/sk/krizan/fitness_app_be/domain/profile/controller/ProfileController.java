package sk.krizan.fitness_app_be.domain.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController implements sk.krizan.fitness_app_be.domain.profile.rest.api.ProfileController {

    private final ProfileService profileService;

    public PageResponse<ProfileDetailResponse> filterProfiles(ProfileFilterRequest request) {
        return profileService.filterProfiles(request);
    }

    public ProfileDetailResponse getProfileById(Long id) {
        return ProfileMapper.entityToResponse(profileService.getProfileById(id));
    }

}
