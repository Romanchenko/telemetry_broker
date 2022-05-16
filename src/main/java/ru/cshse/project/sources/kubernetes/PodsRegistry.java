package ru.cshse.project.sources.kubernetes;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author apollin
 */
@Component
public class PodsRegistry {
    private final Map<String, Pod> registry;

    @Autowired
    public PodsRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public Collection<Pod> getAll() {
        synchronized (registry) {
            return List.copyOf(registry.values());
        }
    }

    public void updateAll(List<Pod> allPods) {
        synchronized (registry) {
            registry.clear();
            for (var pod : allPods) {
                registry.put(pod.getIp(), pod);
            }
        }
    }
}
