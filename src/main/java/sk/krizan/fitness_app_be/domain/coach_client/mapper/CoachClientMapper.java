package sk.krizan.fitness_app_be.domain.coach_client.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

import java.time.Instant;

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
        coach.addToCoaching(coachClient);
        client.addToCoachedBy(coachClient);

        return coachClient;
    }
}
