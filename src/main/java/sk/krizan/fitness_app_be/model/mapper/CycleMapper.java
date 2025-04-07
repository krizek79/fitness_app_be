package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;

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
                .name(cycle.getName())
                .description(cycle.getDescription())
                .numberOfWeeks(cycle.getWeekList() != null ? cycle.getWeekList().size() : 0)
                .build();
    }

    public static Cycle createRequestToEntity(CycleCreateRequest request, Profile currentProfile) {
        Cycle cycle = new Cycle();
        cycle.setName(request.name());
        cycle.setAuthor(currentProfile);
        cycle.setTrainee(currentProfile);
        currentProfile.addToAuthoredCycleList(List.of(cycle));
        return cycle;
    }

    public static Cycle updateRequestToEntity(CycleUpdateRequest request, Cycle cycle) {
        cycle.setName(request.name());
        cycle.setDescription(request.description());
        return cycle;
    }
}
