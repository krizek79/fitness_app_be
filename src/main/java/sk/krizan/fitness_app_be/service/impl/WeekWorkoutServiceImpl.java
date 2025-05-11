package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WeekService;
import sk.krizan.fitness_app_be.service.api.WeekWorkoutService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;
import sk.krizan.fitness_app_be.specification.WeekWorkoutSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekWorkoutServiceImpl implements WeekWorkoutService {

    private final UserService userService;
    private final WeekService weekService;
    private final WorkoutService workoutService;
    private final WeekWorkoutRepository weekWorkoutRepository;

    private final static String ERROR_WEEK_WORKOUT_NOT_FOUND = "WeekWorkout with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            WeekWorkout.Fields.id
    );

    @Override
    @Transactional
    public PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request) {
        Specification<WeekWorkout> specification = WeekWorkoutSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<WeekWorkout> page = weekWorkoutRepository.findAll(specification, pageable);
        List<WeekWorkoutResponse> responseList = page.stream()
                .map(WeekWorkoutMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<WeekWorkoutResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public WeekWorkout getWeekWorkoutById(Long id) {
        return weekWorkoutRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_WEEK_WORKOUT_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public WeekWorkout createWeekWorkout(WeekWorkoutCreateRequest request) {
        Week week = weekService.getWeekById(request.weekId());
        Workout workout = workoutService.getWorkoutById(request.workoutId());
        WeekWorkout weekWorkout = WeekWorkoutMapper.createRequestToEntity(request, week, workout);
        return weekWorkoutRepository.save(weekWorkout);
    }

    @Override
    @Transactional
    public WeekWorkout updateWeekWorkout(Long id, WeekWorkoutUpdateRequest request) {
        User currentUser = userService.getCurrentUser();
        WeekWorkout weekWorkout = getWeekWorkoutById(id);

        if (weekWorkout.getWeek() != null
                && weekWorkout.getWeek().getCycle() != null
                && weekWorkout.getWeek().getCycle().getAuthor() != null
                && weekWorkout.getWeek().getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        return weekWorkoutRepository.save(WeekWorkoutMapper.updateRequestToEntity(request, weekWorkout));
    }

    @Override
    public Long deleteWeekWorkout(Long id) {
        User currentUser = userService.getCurrentUser();
        WeekWorkout weekWorkout = getWeekWorkoutById(id);

        if (weekWorkout.getWeek() == null) {
            throw new RuntimeException("WeekWorkout is null.");
        }

        if (weekWorkout.getWeek().getCycle() != null
                && weekWorkout.getWeek().getCycle().getAuthor() != null
                && weekWorkout.getWeek().getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        weekWorkout.getWeek().removeFromWeekWorkoutList(weekWorkout);
        weekWorkoutRepository.delete(weekWorkout);

        return weekWorkout.getId();
    }

    @Override
    @Transactional
    public WeekWorkout triggerCompleted(Long id) {
        User currentUser = userService.getCurrentUser();
        WeekWorkout weekWorkout = getWeekWorkoutById(id);

        if (weekWorkout.getWeek() != null
                && weekWorkout.getWeek().getCycle() != null
                && weekWorkout.getWeek().getCycle().getAuthor() != null
                && weekWorkout.getWeek().getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }
        weekWorkout.setCompleted(!weekWorkout.getCompleted());
        return weekWorkoutRepository.save(weekWorkout);
    }
}
