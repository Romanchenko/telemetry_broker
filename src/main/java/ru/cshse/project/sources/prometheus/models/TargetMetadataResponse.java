package ru.cshse.project.sources.prometheus.models;

import java.util.List;

import lombok.Data;

/**
 * @author apollin
 */
@Data
public class TargetMetadataResponse {
    private String status;
    private List<MetricDescriptionDto> data;
}
