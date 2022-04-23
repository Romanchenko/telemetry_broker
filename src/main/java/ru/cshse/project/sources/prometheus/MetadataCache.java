package ru.cshse.project.sources.prometheus;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.cshse.project.models.PrometheusMetadataDto;

/**
 * @author apollin
 */
@Component
public class MetadataCache {

    private static final Logger logger = LoggerFactory.getLogger(MetadataCache.class);

    private final ConcurrentHashMap<String, PrometheusMetadataDto> cache;
    private final PrometheusMetadataProvider metadataProvider;

    @Autowired
    public MetadataCache(PrometheusMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
        this.cache = new ConcurrentHashMap<>();
    }

    @Scheduled(fixedDelayString = "${metadata.import.task.fixedDelay.in.milliseconds}")
    public void execute() {
        logger.info("Refreshing metadata cache");
        metadataProvider.get().forEach(meta -> cache.put(meta.getName(), meta));
        logger.info("Cache contains {} entries after update", cache.size());
    }

    public Optional<PrometheusMetadataDto> get(String metricName) {
        return Optional.ofNullable(cache.get(metricName));
    }

    public Collection<PrometheusMetadataDto> getAll() {
        return cache.values();
    }
}
