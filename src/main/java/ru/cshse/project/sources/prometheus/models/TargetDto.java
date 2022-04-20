package ru.cshse.project.sources.prometheus.models;

import lombok.Data;

/**
 * @author apollin
 */
@Data
public class TargetDto {
    private String instance;
    private String job;
}
