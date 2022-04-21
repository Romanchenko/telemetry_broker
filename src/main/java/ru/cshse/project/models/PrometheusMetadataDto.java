package ru.cshse.project.models;

import lombok.Value;

/**
 * @author apollin
 */
@Value
public class PrometheusMetadataDto {
    String name;
    String type;
    String description;
}
