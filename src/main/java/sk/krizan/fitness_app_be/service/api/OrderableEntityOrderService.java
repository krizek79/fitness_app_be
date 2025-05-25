package sk.krizan.fitness_app_be.service.api;

import jakarta.validation.constraints.NotNull;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;

public interface OrderableEntityOrderService {

    void reorder(@NotNull EntityReorderEvent event);

    Class<? extends OrderableEntity> supportedClass();
}
