package sk.krizan.fitness_app_be.domain.cloning;

public abstract class AbstractCloner<T> implements Cloner<T> {

    public abstract Class<T> getHandledClass();
}
