package ru.cshse.project.sources.prometheus.models;

import lombok.Data;

/**
 * @author apollin
 */
@Data
public class MetricResponse {
    private String status;
    private MetricDataDto data;
}
