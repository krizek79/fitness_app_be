package sk.krizan.fitness_app_be.common.order;

public interface OrderableEntity {

    Long getId();
    Integer getOrder();
    void setOrder(Integer order);
}
