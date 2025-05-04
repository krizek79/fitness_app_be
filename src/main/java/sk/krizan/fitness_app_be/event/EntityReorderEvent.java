package sk.krizan.fitness_app_be.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;

@Getter
public class EntityReorderEvent extends ApplicationEvent {

    private final OrderableEntity entity;
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
