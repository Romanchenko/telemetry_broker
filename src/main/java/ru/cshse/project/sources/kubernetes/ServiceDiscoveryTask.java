package ru.cshse.project.sources.kubernetes;

import java.io.IOException;
import java.time.Instant;

import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author apollin
 */
@Component
@ConditionalOnProperty(prefix = "basic.metrics", name = "mechanism", havingValue = "standalone")
public class ServiceDiscoveryTask {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryTask.class);

    private final PodsRegistry registry;
    private final PodsDiscoveryService podsDiscoveryService;

    @Autowired
    public ServiceDiscoveryTask(PodsRegistry registry, PodsDiscoveryService podsDiscoveryService) {
        this.registry = registry;
        this.podsDiscoveryService = podsDiscoveryService;
    }

    @Scheduled(fixedDelayString = "${discovery.task.fixedDelay.in.milliseconds}")
    public void run() throws IOException, ApiException {
        Instant start = Instant.now();
        logger.info("Starting update of endpoints for scraping");
        var allPods = podsDiscoveryService.getAllPods();
        registry.updateAll(allPods);
        logger.info("Finished update of endpoints in {}ms", Instant.now().toEpochMilli() - start.toEpochMilli());
    }
}
