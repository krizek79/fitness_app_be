package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ProfileResponse getProfileById(@PathVariable Long id) {
        return ProfileMapper.entityToResponse(profileService.getProfileById(id));
    }

    @GetMapping("name/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ProfileResponse getProfileByName(@PathVariable String name) {
        return ProfileMapper.entityToResponse(profileService.getProfileByName(name));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteProfile(@PathVariable Long id) {
        return profileService.deleteProfile(id);
    }
}

