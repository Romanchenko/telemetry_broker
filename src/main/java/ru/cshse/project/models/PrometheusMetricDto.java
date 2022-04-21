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
    String type; // todo - change to int enum value
    String instance;
    String job;
    Map<String, String> labels;
}
