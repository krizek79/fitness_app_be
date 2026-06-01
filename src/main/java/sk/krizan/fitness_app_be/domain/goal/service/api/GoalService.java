package sk.krizan.fitness_app_be.domain.goal.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;

public interface GoalService {

    /**
     * Retrieves a paginated list of goals based on the specified filter criteria.
     *
     * @param request the request containing the filter criteria for retrieving goals. The request may include parameters such as page number, page size, sorting options, and various filters based on workout attributes.
     * @return a paginated response containing a list of goals that match the specified filter criteria. The response includes metadata about the pagination, such as total pages, total elements, and the current page number.
     */
    PageResponse<GoalResponse> filterGoals(GoalFilterRequest request);


    /**
     * Creates a new goal or updates an existing goal based on the provided request data.
     *
     * @param id the id of goal to update, or null if creating a new goal
     * @param request the request containing the data for creating or updating the goal
     */
    Goal createUpdateGoal(Long id, GoalInputRequest request);

    /**
     * Deletes the goal with the specified id.
     *
     * @param id the id of the goal to delete
     */
    void deleteGoal(Long id);

}
