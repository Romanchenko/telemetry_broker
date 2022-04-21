package ru.cshse.project.sources.prometheus.models;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author apollin
 */
@Data
public class MetricFields {
    @JsonProperty("__name__")
    private String name;
    private String instance;
    private String job;
    @Nullable
    private String le;
    @Nullable
    private String quantile;
}
