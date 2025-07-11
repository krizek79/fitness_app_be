package sk.krizan.fitness_app_be.cloning;

import lombok.Getter;

import java.util.IdentityHashMap;
import java.util.Map;

@Getter
public class CloneContext {

    private final Map<Object, Object> cache = new IdentityHashMap<>();

    public void cacheClone(Object original, Object clone) {
        cache.put(original, clone);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCachedClone(T original) {
        return (T) cache.get(original);
    }

    public boolean isAlreadyCloned(Object original) {
        return cache.containsKey(original);
    }
}

