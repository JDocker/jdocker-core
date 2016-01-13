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
package io.github.djpaas.machine;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Request that encapsulates the possible parameters to be passed to {@code docker-maschine create <name>}.
 */
public class MachineRequest {
    /** Driver to create machine with. Maps to {@code --driver, -d "none"}. */
    private String driver;

    /** Custom Docker install URL to use for engine installation [$MACHINE_DOCKER_INSTALL_URL], default is
     * {@code https://get.docker.com}. Maps to {@code --engine-install-url}.
     */
    private String installURL;
    /**
     * Specify arbitrary flags to include with the created engine in the form {@code flag=value}. Maps to
     * {@code -engine-opt [--engine-opt option --engine-opt option] }.
     */
     private Set<String> engineOptions = new HashSet<>();
    /** Specify insecure registries to allow with the created engine. Maps to
     * {@code --engine-insecure-registry [--engine-insecure-registry option --engine-insecure-registry option]}.
     */
    private Set<String> insecureRegistries = new HashSet<>();
    /** Labels assigned to the machine, maps to {@code --engine-label [--engine-label option --engine-label option] }. */
    private Set<String> labels = new HashSet<>();
    /** THe storage driver to be used, maps to {@code --engine-storage-driver }. */
    private String storageDriver;
    /** Define environment variables for the engine. Maps to
     * {@code --engine-env [--engine-env option --engine-env option]}. */
    private Set<String> machineEnvironment = new HashSet<>();
    /** Configure Machine with Swarm, maps to {@code --swarm }. */
    private boolean configureMachineWithSwarm = false;
    /** Define the swarm Docker image to be used, maps to {@code --swarm-image "swarm:latest"}. */
    private String swarmImage = "swarm:latest";
    /** Configure machine as swarm-master, maps to {@code  --swarm-master }s. */
    private boolean swarmMaster;
    /** Discovery service to use with Swarm, maps to {@code --swarm-discovery}. */
    private String swarmDiscovery;
    /** Configure the default scheduling strategy for swarm, amps to {@code --swarm-strategy "spread" }. */
    private String swarmStrategy;
    /** Define swarm options for swarm. Maps to
     * {@code --swarm-opt [--swarm-opt option --swarm-opt option]}. */
    private Set<String> swarmEnvironment = new HashSet<>();
    /** ip/socket to listen for a swarn master, maps to {@code --swarm-host "tcp://0.0.0.0:3376"} */
    private URI swarmHostURI;
    /** Address to advertise for swarm, maps to {@code ---swarm-addr "tcp://0.0.0.0:3376"} */
    private URI swarmAdvertizeURI;


}
