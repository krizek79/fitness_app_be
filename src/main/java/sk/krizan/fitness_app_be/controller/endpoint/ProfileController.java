package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.CreateProfileRequest;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LOCAL_USER')")
    public ProfileResponse getProfileById(@PathVariable Long id) {
        return ProfileMapper.profileToResponse(profileService.getProfileById(id));
    }

    @GetMapping("displayName/{displayName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LOCAL_USER')")
    public ProfileResponse getProfileByDisplayName(@PathVariable String displayName) {
        return ProfileMapper.profileToResponse(profileService.getProfileByDisplayName(displayName));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfileResponse createProfile(@Valid @RequestBody CreateProfileRequest request) {
        return ProfileMapper.profileToResponse(profileService.createProfile(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LOCAL_USER')")
    public Long deleteProfile(@PathVariable Long id) {
        return profileService.deleteProfile(id);
    }
}

