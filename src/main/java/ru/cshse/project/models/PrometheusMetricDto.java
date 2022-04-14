package ru.cshse.project.models;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

/**
 * @author apollin
 */
@Value
@Builder(toBuilder = true)
public class PrometheusMetricDto {
    String name;
    BigDecimal value;
    String type;
    String description;
    Map<String, String> labels;
}
