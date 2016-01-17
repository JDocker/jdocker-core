package io.github.jdocker.networking;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     "DNS": {
 * "Domain": "weave.local.",
 * "Upstream": [
 * "192.168.1.1",
 * "62.2.17.61"
 * ],
 * "Address": "172.17.0.1:53",
 * "TTL": 1,
 * "Entries": null
 * }
 * </pre>
 */
public class DNSConfig {
    private String domain;
    private List<String> upstreamNodes = new ArrayList<>();
    private String address;
    private boolean ttl;

    /**
     * Gets domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Gets upstream nodes.
     *
     * @return the upstream nodes
     */
    public List<String> getUpstreamNodes() {
        return upstreamNodes;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Is ttl boolean.
     *
     * @return the boolean
     */
    public boolean isTtl() {
        return ttl;
    }
}
