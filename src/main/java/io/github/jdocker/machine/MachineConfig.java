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
package io.github.jdocker.machine;

import java.util.*;
import java.util.logging.Logger;

import com.spotify.docker.client.*;
import io.github.jdocker.DockerNode;
import io.github.jdocker.DockerNodeRegistry;
import io.github.jdocker.common.Executor;
import io.github.jdocker.common.JSONMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

/**
 * Simple Model of a docker-machine managed by docker docker-machine.
 */
public class MachineConfig {
    private static final Logger LOG = Logger.getLogger(MachineConfig.class.getName());

    /** The docker machine's name. */
    private String name;
    /** Driver to create io.github.jdocker.machine with. Maps to {@code --driver, -d "none"}. */
    private String driver;
    /** Custom DockerNodeRegistry install URL to use for engine installation [$MACHINE_DOCKER_INSTALL_URL], default is
     * {@code https://get.docker.com}. Maps to {@code --engine-install-url}.
     */
    private String installURL;
    /**
     * Specify arbitrary flags to include with the created engine in the form {@code flag=value}. Maps to
     * {@code --engine-opt [--engine-opt option --engine-opt option] }.
     */
    private Set<String> engineOptions = new HashSet<>();
    /** Specify insecure registries to allow with the created engine. Maps to
     * {@code --engine-insecure-registry [--engine-insecure-registry option --engine-insecure-registry option]}.
     */
    private Set<String> insecureRegistries = new HashSet<>();
    /** Labels assigned to the io.github.jdocker.machine, maps to {@code --engine-label [--engine-label option --engine-label option] }. */
    private Set<String> labels = new HashSet<>();
    /** THe storage driver to be used, maps to {@code --engine-storage-driver }. */
    private String storageDriver;
    /** Define environment variables for the engine. Maps to
     * {@code --engine-env [--engine-env option --engine-env option]}. */
    private Set<String> machineEnvironment = new HashSet<>();
    /** Configure MachineConfig with Swarm, maps to {@code --swarm }. */
    private boolean configureMachineWithSwarm = false;
    /** Define the swarm DockerNodeRegistry image to be used, maps to {@code --swarm-image "swarm:latest"}. */
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

    MachineConfig(MachineConfigBuilder builder) {
        this.name = Objects.requireNonNull(builder.name);
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getInstallURL() {
        return installURL;
    }

    public Set<String> getEngineOptions() {
        return Collections.unmodifiableSet(engineOptions);
    }

    public Set<String> getInsecureRegistries() {
        return Collections.unmodifiableSet(insecureRegistries);
    }

    public Set<String> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public String getStorageDriver() {
        return storageDriver;
    }

    public Set<String> getMachineEnvironment() {
        return Collections.unmodifiableSet(machineEnvironment);
    }

    public boolean isConfiguredWithSwarm() {
        return configureMachineWithSwarm;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineConfig machine = (MachineConfig) o;
        return name.equals(machine.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "MachineConfig{" +
                "name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }

    /**
     * Creates a new builder for a machine. Building the machine will also call docker-machine to create the instance.
     * @param name the machine's name, not null.
     * @return the new builder.
     */
    public static MachineConfigBuilder builder(String name){
        return new MachineConfigBuilder(name);
    }
}
