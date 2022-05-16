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
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author apollin
 */
@Service
public class PodsDiscoveryService {

    private final MeterRegistry registry;
    private final String myNodeName;

    @Autowired
    public PodsDiscoveryService(PrometheusMeterRegistry registry, @Value("${current.node.name}") String myNodeName) {
        this.registry = registry;
        this.myNodeName = myNodeName;
    }

    private static final Logger logger = LoggerFactory.getLogger(PodsDiscoveryService.class);

    public List<Pod> getAllPods() throws IOException, ApiException {
        ApiClient client = ClientBuilder.cluster().build();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        List<Pod> pods = new ArrayList<>();
        V1EndpointsList endpoints = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
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

                    if (!myNodeName.equals(ip.getNodeName())) {
                        logger.info("Node name {} is not equal to {}", ip.getNodeName(), myNodeName);
                        continue;
                    }
                    pods.add(new Pod(ip.getIp(), eps.getMetadata().getName(), eps.getMetadata().getNamespace()));
                }
            }
        }
        registry.gauge("podsInDiscovery", pods.size());
        return pods;
    }
}
