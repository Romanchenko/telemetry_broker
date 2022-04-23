package ru.cshse.project.sources.prometheus;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cshse.project.models.Mapper;
import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
public class PrometheusMetricsProvider implements MetricsProvider {
    private static final Logger logger = LoggerFactory.getLogger(PrometheusMetadataProvider.class);

    private final PrometheusClient prometheusClient;
    private final MetadataCache metadataCache;

    @Autowired
    public PrometheusMetricsProvider(PrometheusClient prometheusClient, MetadataCache metadataCache) {
        this.prometheusClient = prometheusClient;
        this.metadataCache = metadataCache;
    }

    @Override
    public Stream<PrometheusMetricDto> get() {
        var allMetrics = metadataCache.getAll();
        logger.info("Will fetch values for {} metrics", allMetrics.size());
        return allMetrics
                .stream()
                .flatMap(metadata -> prometheusClient.getMetric(metadata.getName())
                        .stream()
                        .flatMap(metric -> Mapper.mapMetricsResponse(metric, metadata.getType()).stream())
                );
    }
}
