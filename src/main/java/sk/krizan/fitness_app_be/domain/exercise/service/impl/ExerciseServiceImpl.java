package sk.krizan.fitness_app_be.domain.exercise.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.service.api.EquipmentService;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise.service.api.ExerciseService;
import sk.krizan.fitness_app_be.domain.exercise.specification.ExerciseSpecification;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.service.api.ExerciseMuscleRoleService;
import sk.krizan.fitness_app_be.domain.media.MediaService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final MediaService mediaService;
    private final EquipmentService equipmentService;
    private final ExerciseMuscleRoleService exerciseMuscleRoleService;

    private final ExerciseRepository exerciseRepository;

    private static final List<String> supportedSortFields = List.of(
            Exercise.Fields.id,
            Exercise.Fields.title,
            Exercise.Fields.exerciseCategory
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExerciseSimpleResponse> filterExercises(ExerciseFilterRequest request) {
        Specification<Exercise> specification = ExerciseSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Exercise> page = exerciseRepository.findAll(specification, pageable);
        List<ExerciseSimpleResponse> responseList = page.stream()
                .map(ExerciseMapper::entityToSimpleResponse).collect(Collectors.toList());

        return PageResponse.<ExerciseSimpleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.getByIdOrThrow(id);
    }

    @Override
    @Transactional
    public Exercise createUpdateExercise(Long id, ExerciseInputRequest request, MultipartFile thumbnail) {
        Exercise exercise = id == null ? null : getExerciseById(id);
        exercise = ExerciseMapper.inputRequestToEntity(exercise, request);

        exercise = exerciseRepository.save(exercise);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = mediaService.uploadFile(thumbnail, "exercise-" + exercise.getId());
            exercise.setThumbnailUrl(thumbnailUrl);
        }

        handleEquipment(exercise, request.requiredEquipmentIds());
        handleMuscles(exercise, request.muscles());

        return exerciseRepository.save(exercise);
    }

    private void handleEquipment(Exercise exercise, List<Long> equipmentIds) {
        exercise.getRequiredEquipment().clear();
        equipmentIds.forEach(id -> {
            Equipment equipment = equipmentService.getEquipmentById(id);
            exercise.addToRequiredEquipment(equipment);
        });
    }

    private void handleMuscles(Exercise exercise, List<ExerciseMuscleRoleInputRequest> muscles) {
        Set<Long> incomingIds = muscles.stream().map(ExerciseMuscleRoleInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        // Remove muscles that are not in the incoming request
        exercise.getMuscles().removeIf(muscle -> muscle.getId() != null && !incomingIds.contains(muscle.getId()));

        // Add or update muscles from the incoming request
        for (ExerciseMuscleRoleInputRequest muscleInputRequest : muscles) {
            exerciseMuscleRoleService.createOrUpdateExerciseMuscleRole(exercise, muscleInputRequest);
        }
    }

    @Override
    public void deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exercise.setDeleted(true);
        exerciseRepository.save(exercise);
    }

}
