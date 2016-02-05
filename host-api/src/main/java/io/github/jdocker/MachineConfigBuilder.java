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
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Request that encapsulates the possible parameters to be passed to {@code docker-maschine create <name>}.
 */
public final class MachineConfigBuilder {
    /** Log used. */
    private static final Logger LOG = Logger.getLogger(MachineConfigBuilder.class.getName());
    /** The machine's name. */
    String name;
    /** Driver to create io.github.jdocker.machine with. Maps to {@code --driver, -d "none"}. */
    private String driver;
    /** Custom DockerNodeRegistry install URL to use for engine installation [$MACHINE_DOCKER_INSTALL_URL], default is
     * {@code https://get.docker.com}. Maps to {@code --engine-install-url}.
     */
    private String installURL;

    /**
     * Configure the docker installation targeting the given unpooled machine instance. Unpooled machine's must be
     * accessible using ssh and the given target user must have password free sudo rights so docker-achine can install
     * a docker container onto it.
     */
    private Machine installationTarget;

    /**
     * Specify arbitrary flags to include with the created engine in the form {@code flag=cardinality}. Maps to
     * {@code --engine-opt [--engine-opt option --engine-opt option] }.
     */
     private Set<String> engineOptions = new HashSet<>();
    /** Specify insecure registries to allow with the created engine. Maps to
     * {@code --engine-insecure-registry [--engine-insecure-registry option --engine-insecure-registry option]}.
     */
    private Set<String> insecureRegistries = new HashSet<>();
    /** Labels assigned to the io.github.jdocker.machine, maps to {@code --engine-label [--engine-label option --engine-label option] }. */
    private Map<String,String> labels = new HashMap<String,String>();
    /** THe storage driver to be used, maps to {@code --engine-storage-driver }. */
    private String storageDriver;
    /** Define environment variables for the engine. Maps to
     * {@code --engine-env [--engine-env option --engine-env option]}. */
    private Map<String,String> machineEnvironment = new HashMap<>();
    /** Configure MachineConfig with Swarm, maps to {@code --swarm }. */
    private boolean configureMachineWithSwarm = false;
    /** The optional swarm config. */
    private SwarmConfig swarmConfig;
    /** The basic unpmanaged instance. */
    private Machine unmanagedMachine;

    public MachineConfigBuilder(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public MachineConfigBuilder(Machine machine){
        this.name = Objects.requireNonNull(machine).getName();
        this.installationTarget = machine;
        this.machineEnvironment.putAll(machine.getProperties());
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

    public Map<String,String> getLabels() {
        return Collections.unmodifiableMap(labels);
    }

    public String getStorageDriver() {
        return storageDriver;
    }

    public Map<String,String> getMachineEnvironment() {
        return Collections.unmodifiableMap(machineEnvironment);
    }

    public boolean isConfigureMachineWithSwarm() {
        return configureMachineWithSwarm;
    }

    public SwarmConfig getSwarmConfig() {
        return swarmConfig;
    }

    public MachineConfigBuilder setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public MachineConfigBuilder setInstallUri(String installURL) {
        if(unmanagedMachine!=null){
            throw new IllegalStateException("Cannot set installURL since a unmanaged machine is already assigned as" +
                    "an installation target: " + unmanagedMachine.getUri());
        }
        this.installURL = installURL;
        return this;
    }

    public MachineConfigBuilder addEngineOption(String engineOption) {
        this.engineOptions.add(engineOption);
        return this;
    }

    public MachineConfigBuilder addInsecureRegistry(String insecureRegistry) {
        this.insecureRegistries.add(insecureRegistry);
        return this;
    }

    public MachineConfigBuilder addLabel(String label) {
        this.labels.put(label, label);
        return this;
    }

    public MachineConfigBuilder addLabel(String key, String value) {
        this.labels.put(key, value);
        return this;
    }

    public MachineConfigBuilder setStorageDriver(String storageDriver) {
        this.storageDriver = storageDriver;
        return this;
    }

    public MachineConfigBuilder addMachineEnvProperty(String key, String value) {
        this.machineEnvironment.put(key, value);
        return this;
    }

    public MachineConfigBuilder addMachineEnvProperty(String value) {
        this.machineEnvironment.put(value, value);
        return this;
    }

    public MachineConfigBuilder setSwarmConfig(SwarmConfig swarmConfig) {
        this.swarmConfig = swarmConfig;
        return this;
    }

    /**
     * This method collects all data from the current request and calls {@code docker-machine} to create the instance.
     * @return a new machine instance. Please check the machine's status to evaluate if the creation command was
     * successful {@code status=MachineStatus.Running}.
     */
    public MachineConfig build(){
        return new MachineConfig(this);
    }
}
