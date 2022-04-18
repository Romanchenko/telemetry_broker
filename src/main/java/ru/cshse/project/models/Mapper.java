package ru.cshse.project.models;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .type(prometheusMetricDto.getType())
                .value(prometheusMetricDto.getValue())
                .labels(mapLabels(prometheusMetricDto.getLabels()))
                .description(prometheusMetricDto.getDescription())
                .build();
    }

    private static List<String> mapLabels(Map<String, String> labels) {
        return labels.entrySet()
                .stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue().replace("\"", "'")))
                .collect(Collectors.toList());
    }
}
