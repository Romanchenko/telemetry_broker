package ru.cshse.project.sources.prometheus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cshse.project.sources.prometheus.models.TargetMetadataResponse;

/**
 * @author apollin
 */
public class PrometheusClient {
    private final WebClient webClient;
    private final String baseUrl;
    private final int port;

    @Autowired
    public PrometheusClient(
            @Value("${prometheus.base.url}") String prometheusUrl,
            @Value("${prometheus.port}") int port
    ) {
        this.baseUrl = prometheusUrl;
        this.port = port;
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        this.webClient = WebClient.builder()
                .baseUrl(prometheusUrl)
                .exchangeStrategies(strategies)
                .build();
    }

    public TargetMetadataResponse getTargetsMetadata() {
        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).port(port).path("/api/v1/targets/metadata").build().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TargetMetadataResponse.class)
                .block();
    }
}
