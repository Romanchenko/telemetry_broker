package ru.cshse.project.sources;

import java.util.stream.Stream;

import ru.cshse.project.models.PrometheusMetadataDto;

/**
 * @author apollin
 */
public interface MetadataProvider {

    Stream<PrometheusMetadataDto> get();
}
