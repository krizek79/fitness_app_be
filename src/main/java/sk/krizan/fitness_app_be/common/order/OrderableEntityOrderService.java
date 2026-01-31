package sk.krizan.fitness_app_be.common.order;

import jakarta.validation.constraints.NotNull;
import sk.krizan.fitness_app_be.common.order.event.EntityReorderEvent;

public interface OrderableEntityOrderService {

    void reorder(@NotNull EntityReorderEvent event);

    Class<? extends OrderableEntity> supportedClass();
}
