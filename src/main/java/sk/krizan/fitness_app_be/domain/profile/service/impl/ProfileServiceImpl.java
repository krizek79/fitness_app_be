package sk.krizan.fitness_app_be.domain.profile.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.media.MediaService;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.profile.specification.ProfileSpecification;
import sk.krizan.fitness_app_be.common.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;
    private final MediaService mediaService;

    private final ProfileRepository profileRepository;

    private static final String ERROR_WITH_ID_NOT_FOUND = "Profile with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Profile.Fields.id,
            Profile.Fields.name
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request) {
        Specification<Profile> specification = ProfileSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Profile> page = profileRepository.findAll(specification, pageable);
        List<ProfileResponse> responseList = page.stream()
                .map(ProfileMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<ProfileResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findByIdAndDeletedFalse(id).orElseThrow(
            () -> new ApplicationException(HttpStatus.NOT_FOUND, ERROR_WITH_ID_NOT_FOUND.formatted(id)));
    }

    @Override
    public void deleteProfile(Long id) {
        User currentUser = userService.getCurrentUser();
        Profile profile = getProfileById(id);

        if (profile.getUser() != currentUser && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new ApplicationException(HttpStatus.FORBIDDEN, "");
        }

        profile.setDeleted(true);
        profileRepository.save(profile);
    }

    @Override
    public String uploadProfilePicture(MultipartFile multipartFile) {
        Profile profile = userService.getCurrentUser().getProfile();
        if (profile == null) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "User has no profile.");
        }

        String profilePictureUrl = mediaService.uploadFile(multipartFile, "profile-" + profile.getId());

        profile.setProfilePictureUrl(profilePictureUrl);
        profile = profileRepository.save(profile);

        return profile.getProfilePictureUrl();
    }
}
