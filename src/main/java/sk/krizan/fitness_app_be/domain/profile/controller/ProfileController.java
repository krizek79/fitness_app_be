package sk.krizan.fitness_app_be.domain.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController implements sk.krizan.fitness_app_be.domain.profile.rest.api.ProfileController {

    private final ProfileService profileService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request) {
        return profileService.filterProfiles(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ProfileResponse getProfileById(Long id) {
        return ProfileMapper.entityToResponse(profileService.getProfileById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public Long deleteProfile(Long id) {
        return profileService.deleteProfile(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public String uploadProfilePicture(MultipartFile multipartFile) {
        return profileService.uploadProfilePicture(multipartFile);
    }
}

