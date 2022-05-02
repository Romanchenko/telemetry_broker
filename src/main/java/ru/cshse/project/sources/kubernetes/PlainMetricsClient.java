package ru.cshse.project.sources.kubernetes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author apollin
 */
public class PlainMetricsClient {

    private static final Logger logger = LoggerFactory.getLogger(PlainMetricsClient.class);

    private static final int SIZE = 16 * 1024 * 1024;
    private static final ExchangeStrategies STRATEGIES = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(SIZE))
            .build();
    private static final int DEFAULT_PORT = 15020;
    private static final String DEFAULT_ENDPOINT = "/stats/prometheus";

    public String get(String ip) {
        var localClient = WebClient.builder()
                .baseUrl(UriComponentsBuilder
                        .fromHttpUrl("http://" + ip)
                        .path(DEFAULT_ENDPOINT)
                        .port(DEFAULT_PORT)
                        .build().toString()
                )
                .exchangeStrategies(STRATEGIES)
                .build();
        return localClient
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}