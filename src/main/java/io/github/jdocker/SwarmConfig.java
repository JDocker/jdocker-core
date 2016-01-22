package io.github.jdocker;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by atsticks on 22.01.16.
 */
public final class SwarmConfig {

    private String swarmImage = "swarm:latest";
    /** Configure io.github.jdocker.machine as swarm-master, maps to {@code  --swarm-master }s. */
    private boolean swarmMaster;
    /** Discovery service to use with Swarm, maps to {@code --swarm-discovery}. */
    private String swarmDiscoveryToken;
    /** Configure the default scheduling strategy for swarm, amps to {@code --swarm-strategy "spread" }. */
    private String swarmStrategy;
    /** Define swarm options for swarm. Maps to
     * {@code --swarm-opt [--swarm-opt option --swarm-opt option]}. */
    private Set<String> swarmEnvironment = new HashSet<>();
    /** ip/socket to listen for a swarn master, maps to {@code --swarm-host "tcp://0.0.0.0:3376"} */
    private URI swarmHostURI;
    /** Address to advertise for swarm, maps to {@code ---swarm-addr "tcp://0.0.0.0:3376"} */
    private URI swarmAdvertizeURI;

    public static SwarmConfigBuilder builder(){
        return new SwarmConfigBuilder();
    }

    SwarmConfig(SwarmConfigBuilder builder){
        this.swarmImage = builder.swarmImage;
        this.swarmMaster = builder.swarmMaster;
        this.swarmDiscoveryToken = builder.swarmDiscoveryToken;
        this.swarmStrategy = builder.swarmStrategy;
        this.swarmEnvironment = builder.swarmEnvironment;
        this.swarmHostURI = builder.swarmHostURI;
        this.swarmAdvertizeURI = builder.swarmAdvertizeURI;
    }

    public String getSwarmImage() {
        return swarmImage;
    }

    public boolean isSwarmMaster() {
        return swarmMaster;
    }

    public String getSwarmDiscoveryToken() {
        return swarmDiscoveryToken;
    }

    public String getSwarmStrategy() {
        return swarmStrategy;
    }

    public Set<String> getSwarmEnvironment() {
        return Collections.unmodifiableSet(swarmEnvironment);
    }

    public URI getSwarmHostURI() {
        return swarmHostURI;
    }

    public URI getSwarmAdvertizeURI() {
        return swarmAdvertizeURI;
    }
}
