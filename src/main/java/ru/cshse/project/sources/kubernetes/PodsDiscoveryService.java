package ru.cshse.project.sources.kubernetes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Endpoints;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.util.ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author apollin
 */
public class PodsDiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(PodsDiscoveryService.class);

    public static List<Pod> getAllPods() throws IOException, ApiException {
        ApiClient client = ClientBuilder.cluster().build();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        List<Pod> pods = new ArrayList<>();
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
                    pods.add(new Pod(ip.getIp(), eps.getMetadata().getName(), eps.getMetadata().getNamespace()));
                }
            }
        }
        return pods;
    }
}
