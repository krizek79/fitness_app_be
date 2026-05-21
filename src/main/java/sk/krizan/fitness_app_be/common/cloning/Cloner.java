package sk.krizan.fitness_app_be.common.cloning;

public interface Cloner<T> {
    T clone(T original);
}
