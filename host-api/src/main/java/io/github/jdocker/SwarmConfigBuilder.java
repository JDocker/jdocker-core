/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.jdocker;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Builder for a SwarmConfig.
 */
public final class SwarmConfigBuilder {
    /** Name of the docker host, related to this swarm config, never null.. */
    String dockerHost;
    /** the swarm image used. */
    String swarmImage = "swarm:latest";
    /** Configure io.github.jdocker.machine as swarm-master, maps to {@code  --swarm-master }s. */
    boolean swarmMaster;
    /** Discovery service to use with Swarm, maps to {@code --swarm-discovery}. */
    String swarmDiscoveryToken;
    /** Configure the default scheduling strategy for swarm, amps to {@code --swarm-strategy "spread" }. */
    String swarmStrategy;
    /** Define swarm options for swarm. Maps to
     * {@code --swarm-opt [--swarm-opt option --swarm-opt option]}. */
    Set<String> swarmEnvironment = new HashSet<>();
    /** ip/socket to listen for a swarn master, maps to {@code --swarm-host "tcp://0.0.0.0:3376"} */
    URI swarmHostURI;
    /** Address to advertise for swarm, maps to {@code ---swarm-addr "tcp://0.0.0.0:3376"} */
    URI swarmAdvertizeURI;

    public SwarmConfigBuilder(DockerHost dockerHost){
        this.dockerHost = Objects.requireNonNull(dockerHost.getName());
    }

    public SwarmConfigBuilder setSwarmImage(String swarmImage) {
        this.swarmImage = swarmImage;
        return this;
    }

    public SwarmConfigBuilder setSwarmMaster(boolean swarmMaster) {
        this.swarmMaster = swarmMaster;
        return this;
    }

    public SwarmConfigBuilder setSwarmDiscoveryToken(String swarmDiscoveryToken) {
        this.swarmDiscoveryToken = swarmDiscoveryToken;
        return this;
    }

    public SwarmConfigBuilder setSwarmStrategy(SwarmStrategy strategy) {
        this.swarmStrategy = swarmStrategy;
        return this;
    }

    public SwarmConfigBuilder setSwarmEnvironment(String swarmEnvironment) {
        this.swarmEnvironment.add(swarmEnvironment);
        return this;
    }

    public SwarmConfigBuilder setSwarmHostURI(URI swarmHostURI) {
        this.swarmHostURI = swarmHostURI;
        return this;
    }

    public SwarmConfigBuilder setSwarmAdvertizeURI(URI swarmAdvertizeURI) {
        this.swarmAdvertizeURI = swarmAdvertizeURI;
        return this;
    }

    public SwarmConfig build(){
        return new SwarmConfig(this);
    }
}
