package ru.cshse.project.sources.prometheus;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cshse.project.sources.prometheus.models.MetricResponse;
import ru.cshse.project.sources.prometheus.models.TargetMetadataResponse;

/**
 * @author apollin
 */
public class PrometheusClient {
    private static final Logger logger = LoggerFactory.getLogger(PrometheusClient.class);

    private final WebClient webClient;
    private final String baseUrl;
    private final int port;

    @Autowired
    public PrometheusClient(
            @Value("${prometheus.base.url}") String prometheusUrl,
            @Value("${prometheus.port.my}") String portString
    ) {
        logger.info("Prometheus url {}", prometheusUrl);
        logger.info("Prometheus port {}", portString);
        this.baseUrl = "http://" + prometheusUrl;
        this.port = Integer.parseInt(portString);
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

    public Optional<MetricResponse> getMetric(String metricName) {
        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .port(port)
                        .path("/api/v1/query")
                        .queryParam("query", metricName)
                        .build()
                        .toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MetricResponse.class)
                .blockOptional();
    }
}
