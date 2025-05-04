package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;

public interface OrderableEntityOrderService {

    void reorder(EntityReorderEvent event);

    Class<? extends OrderableEntity> supportedClass();
}
