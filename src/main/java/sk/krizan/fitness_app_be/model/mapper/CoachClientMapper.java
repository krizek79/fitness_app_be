package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;

import java.time.Instant;
import java.util.Set;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoachClientMapper {

    public static CoachClientResponse entityToResponse(CoachClient coachClient) {
        return CoachClientResponse.builder()
                .id(coachClient.getId())
                .coachId(coachClient.getCoach().getId())
                .coachName(coachClient.getCoach().getName())
                .coachPictureUrl(coachClient.getCoach().getProfilePictureUrl())
                .clientId(coachClient.getClient().getId())
                .clientName(coachClient.getClient().getName())
                .clientPictureUrl(coachClient.getClient().getProfilePictureUrl())
                .active(coachClient.getActive())
                .startedAt(coachClient.getStartedAt())
                .build();
    }

    public static CoachClient createRequestToEntity(Profile coach, Profile client) {
        CoachClient coachClient = new CoachClient();
        coachClient.setStartedAt(Instant.now());
        coach.addToCoachingSet(Set.of(coachClient));
        client.addToBeingCoachedSet(Set.of(coachClient));

        return coachClient;
    }
}
