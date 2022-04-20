package ru.cshse.project.sources.prometheus;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
public class PrometheusMetricsProvider implements MetricsProvider {
    private final PrometheusClient prometheusClient;

    @Autowired
    public PrometheusMetricsProvider(PrometheusClient prometheusClient) {
        this.prometheusClient = prometheusClient;
    }

    @Override
    public Stream<PrometheusMetricDto> get() {
        return Stream.empty();
    }
}
