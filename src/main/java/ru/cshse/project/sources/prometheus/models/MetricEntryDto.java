package ru.cshse.project.sources.prometheus.models;

import java.util.List;

import lombok.Data;

/**
 * @author apollin
 */
@Data
public class MetricEntryDto {
    private MetricFields metric;
    private List<Object> value;
}
