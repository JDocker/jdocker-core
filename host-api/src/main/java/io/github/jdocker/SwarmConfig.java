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

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by atsticks on 22.01.16.
 */
public final class SwarmConfig {

    /** Name of the docker host, related to this swarm config. */
    private String dockerHost;
    /** THe swarm image used. */
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

    public static SwarmConfigBuilder builder(DockerHost dockerHost){
        return new SwarmConfigBuilder(dockerHost);
    }

    SwarmConfig(SwarmConfigBuilder builder){
        this.dockerHost = builder.dockerHost;
        this.swarmImage = builder.swarmImage;
        this.swarmMaster = builder.swarmMaster;
        this.swarmDiscoveryToken = builder.swarmDiscoveryToken;
        this.swarmStrategy = builder.swarmStrategy;
        this.swarmEnvironment = builder.swarmEnvironment;
        this.swarmHostURI = builder.swarmHostURI;
        this.swarmAdvertizeURI = builder.swarmAdvertizeURI;
    }

    public String getDockerHost(){
        return dockerHost;
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

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.put("dockerHost", dockerHost);
        o.put("swarmImage", swarmImage);
        o.put("swarmMaster", swarmMaster);
        o.put("swarmDiscoveryToken", swarmDiscoveryToken);
        o.put("swarmStrategy", swarmStrategy);
        o.put("swarmHostUri", swarmHostURI);
        o.put("swarmAdvertizeUri", swarmAdvertizeURI);
        JsonArray array = new JsonArray();
        for(String env:swarmEnvironment){
            array.add(env);
        }
        o.put("swarmEnvironment", array);
        return o;
    }
}
