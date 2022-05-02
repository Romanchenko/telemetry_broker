package ru.cshse.project.sources.kubernetes;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author apollin
 */

public class PodsRegistry {
    private final Map<String, Pod> registry;

    public PodsRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public synchronized void add(Pod pod) {
        registry.put(pod.getIp(), pod);
    }

    public Collection<Pod> getAll() {
        return registry.values();
    }
}
