package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController implements sk.krizan.fitness_app_be.controller.endpoint.api.ProfileController {

    private final ProfileService profileService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request) {
        return profileService.filterProfiles(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ProfileResponse getProfileById(Long id) {
        return ProfileMapper.entityToResponse(profileService.getProfileById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteProfile(Long id) {
        return profileService.deleteProfile(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String uploadProfilePicture(MultipartFile multipartFile) {
        return profileService.uploadProfilePicture(multipartFile);
    }
}

