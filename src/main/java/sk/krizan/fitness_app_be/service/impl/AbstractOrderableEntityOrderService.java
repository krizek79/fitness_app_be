package sk.krizan.fitness_app_be.service.impl;

import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.service.api.OrderableEntityOrderService;

import java.util.List;

public abstract class AbstractOrderableEntityOrderService<T extends OrderableEntity> implements OrderableEntityOrderService {

    @Override
    public void reorder(EntityReorderEvent event) {
        @SuppressWarnings("unchecked")
        T entity = (T) event.getEntity();

        if (getParent(entity) == null) {
            throw new IllegalOperationException("Parent entity is null.");
        }

        switch (event.getEntityLifeCycleEventEnum()) {
            case CREATE -> handleCreate(entity);
            case UPDATE -> handleUpdate(entity, event.getOriginalOrder());
            case DELETE -> handleDelete(entity);
        }
    }

    private void handleCreate(T entity) {
        List<T> list = findAllSiblingsExcludingSelf(entity);
        int desiredOrder = entity.getOrder() != null ? entity.getOrder() : list.size() + 1;

        if (desiredOrder > list.size() + 1) {
            desiredOrder = list.size() + 1;
            entity.setOrder(desiredOrder);
        }

        for (T item : list) {
            if (item.getOrder() >= desiredOrder) {
                item.setOrder(item.getOrder() + 1);
            }
        }

        saveAll(list);
    }

    private void handleUpdate(T entity, Integer originalOrder) {
        if (entity.getOrder() == null) {
            throw new IllegalOperationException("Entity order cannot be null.");
        }

        int newOrder = entity.getOrder();
        if (originalOrder == newOrder) return;

        List<T> list = findAllSiblings(entity);

        if (newOrder < originalOrder) {
            for (T item : list) {
                if (!item.getId().equals(entity.getId())
                        && item.getOrder() >= newOrder
                        && item.getOrder() < originalOrder) {
                    item.setOrder(item.getOrder() + 1);
                }
            }
        } else {
            for (T item : list) {
                if (!item.getId().equals(entity.getId())
                        && item.getOrder() <= newOrder
                        && item.getOrder() > originalOrder) {
                    item.setOrder(item.getOrder() - 1);
                }
            }
        }

        saveAll(list);
    }

    private void handleDelete(T entity) {
        Integer deletedOrder = entity.getOrder();
        if (deletedOrder == null) return;

        List<T> list = findAllSiblingsExcludingSelf(entity);
        for (T item : list) {
            if (item.getOrder() > deletedOrder) {
                item.setOrder(item.getOrder() - 1);
            }
        }

        saveAll(list);
    }

    protected abstract Object getParent(T entity);
    protected abstract List<T> findAllSiblings(T entity);
    protected abstract List<T> findAllSiblingsExcludingSelf(T entity);
    protected abstract void saveAll(List<T> entities);
}
