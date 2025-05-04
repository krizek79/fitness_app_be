package sk.krizan.fitness_app_be.listener;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.service.api.OrderableEntityOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderableEntityReorderEventListener {

    private final List<OrderableEntityOrderService> orderServices;

    private Map<Class<? extends OrderableEntity>, OrderableEntityOrderService> serviceByClass;

    @PostConstruct
    public void init() {
        serviceByClass = new HashMap<>();
        for (OrderableEntityOrderService service : orderServices) {
            serviceByClass.put(service.supportedClass(), service);
        }
    }

    @EventListener
    public void handleEntityReorder(EntityReorderEvent event) {
        OrderableEntity entity = event.getEntity();
        if (entity == null) {
            log.warn("Received reorder event with null entity.");
            return;
        }

        OrderableEntityOrderService service = serviceByClass.get(entity.getClass());
        if (service != null) {
            service.reorder(event);
        } else {
            log.warn("No service class found to handle entity reorder logic: {}.", entity.getClass().getSimpleName());
        }
    }
}
