package ru.cshse.project.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.cshse.project.sources.MetricsProvider;
import ru.cshse.project.sources.prometheus.models.MetricDescriptionDto;
import ru.cshse.project.sources.prometheus.models.MetricResponse;
import ru.cshse.project.sources.prometheus.models.TargetDto;

/**
 * @author apollin
 */
public class Mapper {

    private Mapper() {
    }

    public static CHMetricDto map(PrometheusMetricDto prometheusMetricDto) {
        return CHMetricDto.builder()
                .id(UUID.randomUUID())
                .ts(Instant.now().toEpochMilli())
                .name(prometheusMetricDto.getName())
                .value(prometheusMetricDto.getValue())
                .labels(mapLabels(prometheusMetricDto.getLabels()))
                .build();
    }

    private static List<String> mapLabels(Map<String, String> labels) {
        return labels.entrySet()
                .stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue().replace("\"", "'")))
                .collect(Collectors.toList());
    }

    public static List<PrometheusMetricDto> mapMetricsResponse(MetricResponse response, String type) {
        return response.getData()
                .getResult()
                .stream()
                .map(data -> PrometheusMetricDto.builder()
                        .name(data.getMetric().getName())
                        .instance(data.getMetric().getInstance())
                        .job(data.getMetric().getJob())
                        .labels(Map.of())
                        .type(type)
                        .value(BigDecimal.valueOf(Double.parseDouble((String) data.getValue().get(1))))
                        .build()
                )
                .collect(Collectors.toList());
    }
}
