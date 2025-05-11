package sk.krizan.fitness_app_be.cloning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CloneOrchestrator {

    private final Map<Class<?>, Cloner<?>> clonerMap;

    @Autowired
    public CloneOrchestrator(List<AbstractCloner<?>> cloners) {
        this.clonerMap = cloners.stream()
                .collect(Collectors.toMap(AbstractCloner::getHandledClass, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T> T deepClone(T original, CloneContext context) {
        Cloner<T> cloner = (Cloner<T>) clonerMap.get(original.getClass());
        if (cloner == null) {
            throw new IllegalArgumentException("No cloner registered for " + original.getClass());
        }
        return cloner.clone(original, context);
    }
}
