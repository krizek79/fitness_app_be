package sk.krizan.fitness_app_be.domain.cloning;

public interface Cloner<T> {
    T clone(T original);
}
