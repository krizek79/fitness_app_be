package sk.krizan.fitness_app_be.common.cloning;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CloneOrchestrator {

    private final Map<Class<?>, Cloner<?>> clonerMap;

    public CloneOrchestrator(List<AbstractCloner<?>> cloners) {
        this.clonerMap = cloners.stream()
                .collect(Collectors.toMap(AbstractCloner::getHandledClass, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T> T deepClone(T original) {
        Class<?> clazz = Hibernate.getClass(original);
        Cloner<T> cloner = (Cloner<T>) clonerMap.get(clazz);

        if (cloner == null) {
            throw new IllegalArgumentException("No cloner registered for " + original.getClass());
        }
        return cloner.clone(original);
    }
}
