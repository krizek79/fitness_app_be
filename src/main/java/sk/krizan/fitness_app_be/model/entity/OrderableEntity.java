package sk.krizan.fitness_app_be.model.entity;

public interface OrderableEntity {

    Long getId();
    Integer getOrder();
    void setOrder(Integer order);
}
