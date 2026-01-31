package sk.krizan.fitness_app_be.domain.cycle.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CycleMapper {

    public static CycleResponse entityToResponse(Cycle cycle) {
        return CycleResponse.builder()
                .id(cycle.getId())
                .authorId(cycle.getAuthor() != null ? cycle.getAuthor().getId() : null)
                .authorName(cycle.getAuthor() != null ? cycle.getAuthor().getName() : null)
                .traineeId(cycle.getTrainee() != null ? cycle.getTrainee().getId() : null)
                .traineeName(cycle.getTrainee() != null ? cycle.getTrainee().getName() : null)
                .name(cycle.getTitle())
                .description(cycle.getDescription())
                .numberOfWeeks(cycle.getWeekList() != null ? cycle.getWeekList().size() : 0)
                .levelResponse(cycle.getLevel() != null ? ReferenceDataMapper.enumToResponse(cycle.getLevel()) : null)
                .build();
    }

    public static Cycle createRequestToEntity(CycleCreateRequest request, Profile currentProfile, Profile trainee) {
        Cycle cycle = new Cycle();
        cycle.setTitle(request.title());
        cycle.setAuthor(currentProfile);
        cycle.setTrainee(trainee);
        currentProfile.addToAuthoredCycleList(List.of(cycle));
        currentProfile.addToAssignedCycleList(List.of(cycle));
        return cycle;
    }

    public static Cycle updateRequestToEntity(CycleUpdateRequest request, Cycle cycle, Profile trainee) {
        cycle.setTrainee(trainee);
        cycle.setTitle(request.title());
        cycle.setDescription(request.description());
        cycle.setLevel(request.level());
        return cycle;
    }
}
