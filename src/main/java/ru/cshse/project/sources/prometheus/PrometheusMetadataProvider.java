package ru.cshse.project.sources.prometheus;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cshse.project.models.PrometheusMetadataDto;
import ru.cshse.project.sources.MetadataProvider;

/**
 * @author apollin
 */
@Component
public class PrometheusMetadataProvider implements MetadataProvider {

    private final PrometheusClient prometheusClient;

    @Autowired
    public PrometheusMetadataProvider(PrometheusClient prometheusClient) {
        this.prometheusClient = prometheusClient;
    }

    @Override
    public Stream<PrometheusMetadataDto> get() {
        var response = prometheusClient.getTargetsMetadata();
        return response.getData().stream().map(data ->
                new PrometheusMetadataDto(data.getMetric(), data.getType(), data.getHelp()));
    }
}
