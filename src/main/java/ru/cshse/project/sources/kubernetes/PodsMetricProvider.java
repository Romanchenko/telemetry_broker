package ru.cshse.project.sources.kubernetes;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Endpoints;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author apollin
 */
public class PodsMetricProvider {

    private static final Logger logger = LoggerFactory.getLogger(PodsMetricProvider.class);

    public static void getAllPods() throws IOException, ApiException {
        ApiClient client = ClientBuilder.cluster().build();
        Configuration.setDefaultApiClient(client);
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        CoreV1Api api = new CoreV1Api();
        V1EndpointsList endpoints = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null);
        for (V1Endpoints eps : endpoints.getItems()) {
            var subsets = eps.getSubsets();
            if (subsets == null) {
                logger.warn("Subsets is null");
                continue;
            }
            for (var subset : subsets) {
                if (subset == null) {
                    logger.warn("Subset in subsets {} is null", eps.getMetadata().getName());
                    continue;
                }
                for (var ip: subset.getAddresses()) {
                    logger.info("Endpoints set {}, namespace {}, address {}, ip {}", eps.getMetadata().getName(), eps.getMetadata().getNamespace(), ip.getHostname(), ip.getIp());
                    var localClient = WebClient.builder()
                            .baseUrl(UriComponentsBuilder
                                    .fromHttpUrl("http://" + ip.getIp())
                                    .path("/stats/prometheus")
                                    .port(15020
                                    ).build().toString()
                            )
                            .exchangeStrategies(strategies)
                            .build();
                    try {
                        var result = localClient
                                .get()
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();
                        logger.info("Got result : {}", result);
                    } catch (Exception e) {
                        logger.error("Got error while requesting metrics", e);
                    }


                }
            }
        }
        V1PodList list =
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
        logger.info("About to get all pods");
        for (V1Pod item : list.getItems()) {
            logger.info("Got pod {}", item.getMetadata().getName());
        }
    }
}
