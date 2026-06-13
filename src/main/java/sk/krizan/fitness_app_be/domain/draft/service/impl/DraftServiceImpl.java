package sk.krizan.fitness_app_be.domain.draft.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;
import sk.krizan.fitness_app_be.domain.draft.mapper.DraftMapper;
import sk.krizan.fitness_app_be.domain.draft.repository.DraftRepository;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;
import sk.krizan.fitness_app_be.domain.draft.service.api.DraftService;
import sk.krizan.fitness_app_be.domain.draft.specification.DraftSpecification;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DraftServiceImpl implements DraftService {

    private final UserService userService;

    private final DraftRepository draftRepository;

    private static final List<String> supportedSortFields = List.of(
            Draft.Fields.id,
            Draft.Fields.title
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DraftResponse> filterDrafts(DraftFilterRequest request) {
        User currentUser = userService.getOrCreateCurrentUser();
        boolean isUserAdmin = userService.isUserAdmin(currentUser);

        Profile currentProfile = currentUser.getProfile();
        Specification<Draft> specification = DraftSpecification.filter(request, currentProfile, isUserAdmin);

        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );

        Page<Draft> page = draftRepository.findAll(specification, pageable);

        List<DraftResponse> responseList = page.stream()
                .map(DraftMapper::entityToResponse)
                .collect(Collectors.toList());

        return PageResponse.<DraftResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Draft getDraftById(Long id) {
        return draftRepository.getByIdOrThrow(id);
    }

    @Override
    @Transactional
    public Draft createDraft(DraftCreateRequest request) {
        User currentUser = userService.getOrCreateCurrentUser();
        Profile currentProfile = currentUser.getProfile();

        Draft draft = DraftMapper.createRequestToEntity(request, currentProfile);
        draft.setProfile(currentProfile);

        return draftRepository.save(draft);
    }

    @Override
    @Transactional
    public Draft updateDraft(Long id, DraftUpdateRequest request) {
        Draft draft = getDraftById(id);
        DraftMapper.updateRequestToEntity(draft, request);
        return draftRepository.save(draft);
    }

    @Override
    @Transactional
    public void deleteDraft(Long id) {
        Draft draft = getDraftById(id);
        draftRepository.delete(draft);
    }

}
