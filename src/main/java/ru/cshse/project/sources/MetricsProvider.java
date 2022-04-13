package ru.cshse.project.sources;

import java.util.stream.Stream;

import ru.cshse.project.models.PrometheusMetricDto;

/**
 * @author apollin
 */
public interface MetricsProvider {

    Stream<PrometheusMetricDto> get();
}
