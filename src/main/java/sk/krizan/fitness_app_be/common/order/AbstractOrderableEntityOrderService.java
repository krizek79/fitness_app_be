package sk.krizan.fitness_app_be.common.order;

import org.springframework.http.HttpStatus;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.common.order.event.EntityReorderEvent;

import java.util.List;
import java.util.Objects;

public abstract class AbstractOrderableEntityOrderService<T extends OrderableEntity> implements OrderableEntityOrderService {

    @Override
    public void reorder(EntityReorderEvent event) {
        @SuppressWarnings("unchecked")
        T entity = (T) event.getEntity();

        if (getParent(entity) == null) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Parent entity is null.");
        }

        switch (event.getEntityLifeCycleEventEnum()) {
            case CREATE -> handleCreate(entity);
            case UPDATE -> handleUpdate(entity, event.getOriginalOrder());
            case DELETE -> handleDelete(entity);
        }
    }

    private void handleCreate(T entity) {
        List<T> siblings = findAllSiblings(entity);
        int totalCount = siblings.size();

        int maxOrder = entity.getId() == null ? totalCount + 1 : totalCount;
        int desiredOrder = entity.getOrder() != null ? Math.min(entity.getOrder(), maxOrder) : maxOrder;

        entity.setOrder(desiredOrder);

        for (T item : siblings) {
            if (!item.getId().equals(entity.getId()) && item.getOrder() >= desiredOrder) {
                item.setOrder(item.getOrder() + 1);
            }
        }

        saveAll(siblings);
    }

    private void handleUpdate(T entity, Integer originalOrder) {
        if (entity.getOrder() == null || originalOrder == null) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Order values cannot be null.");
        }

        List<T> siblings = findAllSiblings(entity);
        int newOrder = Math.min(entity.getOrder(), siblings.size());
        entity.setOrder(newOrder);

        if (Objects.equals(originalOrder, newOrder)) return;

        if (newOrder < originalOrder) {
            for (T item : siblings) {
                if (!item.getId().equals(entity.getId())
                        && item.getOrder() >= newOrder
                        && item.getOrder() < originalOrder) {
                    item.setOrder(item.getOrder() + 1);
                }
            }
        } else {
            for (T item : siblings) {
                if (!item.getId().equals(entity.getId())
                        && item.getOrder() <= newOrder
                        && item.getOrder() > originalOrder) {
                    item.setOrder(item.getOrder() - 1);
                }
            }
        }

        saveAll(siblings);
    }

    private void handleDelete(T entity) {
        Integer deletedOrder = entity.getOrder();
        if (deletedOrder == null) return;

        List<T> siblings = findAllSiblings(entity);
        for (T item : siblings) {
            if (!item.getId().equals(entity.getId()) && item.getOrder() > deletedOrder) {
                item.setOrder(item.getOrder() - 1);
            }
        }

        saveAll(siblings);
    }

    protected abstract Object getParent(T entity);

    protected abstract List<T> findAllSiblings(T entity);

    protected abstract void saveAll(List<T> entities);
}