package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.draft.DraftCreateRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.DraftResponse;
import sk.krizan.fitness_app_be.model.entity.Draft;
import sk.krizan.fitness_app_be.model.entity.Profile;

import java.util.Map;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DraftMapper {

    public static DraftResponse entityToResponse(Draft draft) {
        return DraftResponse.builder()
                .id(draft.getId())
                .entityType(draft.getEntityType())
                .profileId(draft.getProfile().getId())
                .title(draft.getTitle())
                .content(draft.getContent())
                .build();
    }

    public static Draft createRequestToEntity(
            DraftCreateRequest request,
            Profile profile
    ) {
        Draft draft = new Draft();
        draft.setEntityType(request.entityType());

        Map<String, Object> requestContent = request.content();
        draft.setContent(requestContent);
        setTitleFromContent(requestContent, draft);

        draft.setProfile(profile);

        return draft;
    }

    public static void updateRequestToEntity(
            Draft draft,
            DraftUpdateRequest request
    ) {
        Map<String, Object> requestContent = request.content();
        draft.setContent(requestContent);
        setTitleFromContent(requestContent, draft);
    }

    private static void setTitleFromContent(Map<String, Object> content, Draft draft) {
        String title = (String) content.getOrDefault("title", "Untitled");
        draft.setTitle(title);
    }
}
