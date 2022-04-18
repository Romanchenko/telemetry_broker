package ru.cshse.project.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

/**
 * @author apollin
 */
@Value
@Builder(toBuilder = true)
public class CHMetricDto {
    UUID id;
    long ts;
    String name;
    String type;
    BigDecimal value;
    List<String> labels;
    String description;
}
