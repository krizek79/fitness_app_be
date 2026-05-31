package sk.krizan.fitness_app_be.domain.week.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.mapper.WeekMapper;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;

@Service
@RequiredArgsConstructor
public class WeekServiceImpl implements WeekService {

    private final WeekRepository weekRepository;

    @Override
    public Week getWeekById(Long id) {
        return weekRepository.getByIdOrThrow(id);
    }

    @Override
    public void createUpdateWeek(Plan plan, WeekInputRequest weekInputRequest) {
        Week week;
        if (weekInputRequest.id() != null) {
            //  Update existing week
            week = plan.getWeeks().stream()
                    .filter(we -> we.getId().equals(weekInputRequest.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Week with id %d not found in plan with id %d.".formatted(weekInputRequest.id(), plan.getId())));
            WeekMapper.inputRequestToEntity(week, weekInputRequest, plan);
        } else {
            //  Create new week
            WeekMapper.inputRequestToEntity(null, weekInputRequest, plan);
        }
    }
}
