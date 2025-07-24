package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ApplicationException;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.ExerciseService;
import sk.krizan.fitness_app_be.specification.ExerciseSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final EnumService enumService;

    private final ExerciseRepository exerciseRepository;

    private final static String ERROR_EXERCISE_NOT_FOUND = "Exercise with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Exercise.Fields.id,
            Exercise.Fields.name
    );

    @Override
    @Transactional
    public PageResponse<ExerciseResponse> filterExercises(ExerciseFilterRequest request) {
        Specification<Exercise> specification = ExerciseSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Exercise> page = exerciseRepository.findAll(specification, pageable);
        List<ExerciseResponse> responseList = page.stream()
                .map(ExerciseMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<ExerciseResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, ERROR_EXERCISE_NOT_FOUND.formatted(id)));
    }

    @Override
    public Exercise createExercise(ExerciseCreateRequest request) {
        Set<MuscleGroup> muscleGroups = request.muscleGroupKeys().stream()
            .map(key -> enumService.findEnumByKey(MuscleGroup.class, key))
            .collect(Collectors.toSet());
        Exercise exercise = ExerciseMapper.createRequestToEntity(request, muscleGroups);
        return exerciseRepository.save(exercise);
    }

    @Override
    public Long deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exerciseRepository.delete(exercise);
        return exercise.getId();
    }
}
