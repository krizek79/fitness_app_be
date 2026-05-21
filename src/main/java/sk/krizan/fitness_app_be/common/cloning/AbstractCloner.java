package sk.krizan.fitness_app_be.common.cloning;

public abstract class AbstractCloner<T> implements Cloner<T> {

    public abstract Class<T> getHandledClass();
}
