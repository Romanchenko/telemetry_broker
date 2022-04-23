package ru.cshse.project.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cshse.project.sources.prometheus.models.MetricResponse;

/**
 * @author apollin
 */
public class Mapper {

    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);
    private static final String NAN = "NaN";
    private static final String PLUS_INF = "+Inf";
    private static final String MINUS_INF = "-Inf";
    private static final double MAX_VALUE = Math.pow(10, 30);
    private static final double MIN_VALUE = -Math.pow(10, 30);

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
                .filter(data -> !data.getValue().get(1).equals(NAN))
                .map(data -> PrometheusMetricDto.builder()
                                 .name(data.getMetric().getName())
                                 .instance(data.getMetric().getInstance())
                                 .job(data.getMetric().getJob())
                                 .labels(Map.of())
                                 .type(type)
                                 .value(BigDecimal.valueOf(getDouble((String) data.getValue().get(1))))
                                 .build()
                )
                .collect(Collectors.toList());
    }

    private static double getDouble(String value) {
        if (value.equals(PLUS_INF)) {
            return MAX_VALUE;
        }
        if (value.equals(MINUS_INF)) {
            return MIN_VALUE;
        }
        return Double.parseDouble(value);
    }
}
