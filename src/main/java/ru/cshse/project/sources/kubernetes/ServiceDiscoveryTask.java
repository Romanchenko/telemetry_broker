package ru.cshse.project.sources.kubernetes;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author apollin
 */
@Component
public class ServiceDiscoveryTask {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryTask.class);

    @Scheduled(fixedDelayString = "${import.task.fixedDelay.in.milliseconds}")
    public void run() throws IOException, ApiException {
        logger.info("Running test pods metric collector task");
        PodsMetricProvider.getAllPods();
    }
}
