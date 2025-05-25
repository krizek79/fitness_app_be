package sk.krizan.fitness_app_be.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.service.api.OrderableEntityOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekOrderService implements OrderableEntityOrderService {

    private final WeekRepository weekRepository;

    @Override
    @Transactional
    public void reorder(@NotNull EntityReorderEvent event) {
        Week modifiedWeek = (Week) event.getEntity();
        if (modifiedWeek.getCycle() == null) {
            throw new IllegalOperationException("Cycle is null.");
        }

        switch (event.getEntityLifeCycleEventEnum()) {
            case CREATE -> handleCreate(modifiedWeek);
            case UPDATE -> handleUpdate(modifiedWeek, event.getOriginalOrder());
            case DELETE -> handleDelete(modifiedWeek);
        }
    }

    private void handleCreate(Week newWeek) {
        List<Week> weeks = weekRepository.findAllByCycleIdAndIdNotOrderByOrder(newWeek.getCycle().getId(), newWeek.getId());
        int desiredOrder = newWeek.getOrder() != null ? newWeek.getOrder() : weeks.size() + 1;

        if (desiredOrder > weeks.size() + 1) {
            desiredOrder = weeks.size() + 1;
            newWeek.setOrder(desiredOrder);
        }

        for (Week week : weeks) {
            if (week.getOrder() >= desiredOrder) {
                week.setOrder(week.getOrder() + 1);
            }
        }

        weekRepository.saveAll(weeks);
    }

    private void handleUpdate(Week updatedWeek, int originalOrder) {
        if (updatedWeek.getOrder() == null) {
            throw new IllegalOperationException("Updated week's order cannot be null.");
        }

        int newOrder = updatedWeek.getOrder();
        if (originalOrder == newOrder) {
            return;
        }

        List<Week> weeks = weekRepository.findAllByCycleIdOrderByOrder(updatedWeek.getCycle().getId());

        if (newOrder < originalOrder) {
            for (Week week : weeks) {
                if (!week.getId().equals(updatedWeek.getId())
                        && week.getOrder() >= newOrder
                        && week.getOrder() < originalOrder) {
                    week.setOrder(week.getOrder() + 1);
                }
            }
        } else {
            for (Week week : weeks) {
                if (!week.getId().equals(updatedWeek.getId())
                        && week.getOrder() <= newOrder
                        && week.getOrder() > originalOrder) {
                    week.setOrder(week.getOrder() - 1);
                }
            }
        }

        weekRepository.saveAll(weeks);
    }

    private void handleDelete(Week deletedWeek) {
        Integer deletedOrder = deletedWeek.getOrder();
        if (deletedOrder == null) {
            return;
        }

        List<Week> weeks = weekRepository.findAllByCycleIdAndIdNotOrderByOrder(deletedWeek.getCycle().getId(), deletedWeek.getId());
        for (Week week : weeks) {
            if (week.getOrder() > deletedOrder) {
                week.setOrder(week.getOrder() - 1);
            }
        }

        weekRepository.saveAll(weeks);
    }

    @Override
    public Class<? extends OrderableEntity> supportedClass() {
        return Week.class;
    }
}
