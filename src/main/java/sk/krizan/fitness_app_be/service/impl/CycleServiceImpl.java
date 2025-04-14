package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.CycleMapper;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.service.api.CycleService;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.specification.CycleSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final UserService userService;
    private final EnumService enumService;
    private final CycleRepository cycleRepository;

    private final static String ERROR_CYCLE_NOT_FOUND = "Cycle with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Cycle.Fields.id,
            Cycle.Fields.name
    );

    @Override
    @Transactional
    public PageResponse<CycleResponse> filterCycles(CycleFilterRequest request) {
        Specification<Cycle> specification = CycleSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Cycle> page = cycleRepository.findAll(specification, pageable);
        List<CycleResponse> responseList = page.stream()
                .map(CycleMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<CycleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Cycle getCycleById(Long id) {
        return cycleRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_CYCLE_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public Cycle createCycle(CycleCreateRequest request) {
        User currentUser = userService.getCurrentUser();
        Profile profile = currentUser.getProfile();
        Cycle cycle = CycleMapper.createRequestToEntity(request, profile);
        return cycleRepository.save(cycle);
    }

    @Override
    @Transactional
    public Cycle updateCycle(Long id, CycleUpdateRequest request) {
        Cycle cycle = getCycleById(id);
        User currentUser = userService.getCurrentUser();
        if (cycle.getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        Level level = (Level) enumService.findEnumByKey(request.levelKey());

        return cycleRepository.save(CycleMapper.updateRequestToEntity(request, cycle, level));
    }

    @Override
    public Long deleteCycle(Long id) {
        User currentUser = userService.getCurrentUser();
        Cycle cycle = getCycleById(id);

        if (cycle.getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        cycleRepository.delete(cycle);
        return cycle.getId();
    }
}
