package ru.cshse.project.sources.kubernetes;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * @author apollin
 */
@Component
public class PodsRegistry {
    private final Map<String, Pod> registry;

    public PodsRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public synchronized void add(Pod pod) {
        registry.put(pod.getIp(), pod);
    }

    public Collection<Pod> getAll() {
        return List.copyOf(registry.values());
    }

    public synchronized void updateAll(List<Pod> allPods) {
        registry.clear();
        for (var pod : allPods) {
            registry.put(pod.getIp(), pod);
        }
    }
}
