package sk.krizan.fitness_app_be.cloning;

public interface Cloner<T> {
    T clone(T original);
}
