package io.github.jdocker.networking;

/**
 * Weave networking config.
 */
public class NetworkConfig {

    private String weaveVersion;
    private RouterConfig routerConfig;
    private IPAMConfig ipamConfig;
    private DNSConfig dnsCOnfig;

    public String getWeaveVersion() {
        return weaveVersion;
    }

    public RouterConfig getRouterConfig() {
        return routerConfig;
    }

    public IPAMConfig getIpamConfig() {
        return ipamConfig;
    }

    public DNSConfig getDnsCOnfig() {
        return dnsCOnfig;
    }
}
