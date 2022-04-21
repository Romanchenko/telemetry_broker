package ru.cshse.project.sources.prometheus.models;

import java.util.List;

import lombok.Data;

/**
 * @author apollin
 */
@Data
public class MetricDataDto {
    private String resultType;
    private List<MetricEntryDto> result;
}
