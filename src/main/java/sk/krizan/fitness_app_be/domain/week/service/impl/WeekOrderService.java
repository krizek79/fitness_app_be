package sk.krizan.fitness_app_be.domain.week.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.order.OrderableEntity;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.common.order.AbstractOrderableEntityOrderService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeekOrderService extends AbstractOrderableEntityOrderService<Week> {

    private final WeekRepository weekRepository;


    @Override
    protected Object getParent(Week entity) {
        return entity.getCycle();
    }

    @Override
    protected List<Week> findAllSiblings(Week entity) {
        return weekRepository.findAllByCycleIdOrderByOrder(entity.getCycle().getId());
    }

    @Override
    protected void saveAll(List<Week> entities) {
        weekRepository.saveAll(entities);
    }

    @Override
    public Class<? extends OrderableEntity> supportedClass() {
        return Week.class;
    }
}
