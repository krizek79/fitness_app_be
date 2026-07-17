package sk.krizan.fitness_app_be.domain.profile.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;
import sk.krizan.fitness_app_be.domain.profile.specification.ProfileSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private static final List<String> supportedSortFields = List.of(
            Profile.Fields.id,
            Profile.Fields.name
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfileDetailResponse> filterProfiles(ProfileFilterRequest request) {
        Specification<Profile> specification = ProfileSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Profile> page = profileRepository.findAll(specification, pageable);
        List<ProfileDetailResponse> responseList = page.stream()
                .map(ProfileMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<ProfileDetailResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.getByIdOrThrow(id);
    }

    @Override
    public Profile getProfileByPublicId(String publicId) {
        return profileRepository.getByPublicIdOrThrow(publicId);
    }

}
