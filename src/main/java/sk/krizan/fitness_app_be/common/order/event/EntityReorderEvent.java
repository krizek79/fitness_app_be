package sk.krizan.fitness_app_be.common.order.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sk.krizan.fitness_app_be.common.order.OrderableEntity;

@Getter
public class EntityReorderEvent extends ApplicationEvent {

    @NotNull
    private final OrderableEntity entity;
    @NotNull
    private final EntityLifeCycleEventEnum entityLifeCycleEventEnum;
    private final Integer originalOrder;

    public EntityReorderEvent(OrderableEntity source, EntityLifeCycleEventEnum entityLifeCycleEventEnum, Integer originalOrder) {
        super(source);
        this.entity = source;
        this.entityLifeCycleEventEnum = entityLifeCycleEventEnum;
        this.originalOrder = originalOrder;
    }

    public EntityReorderEvent(OrderableEntity entity, EntityLifeCycleEventEnum eventType) {
        this(entity, eventType, null);
    }
}
