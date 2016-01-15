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

import io.github.jdocker.common.Executor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Request that encapsulates the possible parameters to be passed to {@code docker-maschine create <name>}.
 */
public class MachineBuilder {
    /** Log used. */
    private static final Logger LOG = Logger.getLogger(MachineBuilder.class.getName());
    /** The machine's name. */
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
    /** Configure Machine with Swarm, maps to {@code --swarm }. */
    private boolean configureMachineWithSwarm = false;
    /** Define the swarm DockerNodeRegistry image to be used, maps to {@code --swarm-image "swarm:latest"}. */
    private String swarmImage = "swarm:latest";
    /** Configure io.github.jdocker.machine as swarm-master, maps to {@code  --swarm-master }s. */
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


    MachineBuilder(String name) {
        this.name = Objects.requireNonNull(name);
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

    public boolean isConfigureMachineWithSwarm() {
        return configureMachineWithSwarm;
    }

    public String getSwarmImage() {
        return swarmImage;
    }

    public boolean isSwarmMaster() {
        return swarmMaster;
    }

    public String getSwarmDiscovery() {
        return swarmDiscovery;
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

    public MachineBuilder setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public MachineBuilder setInstallURL(String installURL) {
        this.installURL = installURL;
        return this;
    }

    public MachineBuilder addEngineOption(String engineOption) {
        this.engineOptions.add(engineOption);
        return this;
    }

    public MachineBuilder addInsecureRegistry(String insecureRegistry) {
        this.insecureRegistries.add(insecureRegistry);
        return this;
    }

    public MachineBuilder addLabel(String label) {
        this.labels.add(label);
        return this;
    }

    public MachineBuilder setStorageDriver(String storageDriver) {
        this.storageDriver = storageDriver;
        return this;
    }

    public MachineBuilder addMachineEnvProperty(String machineEnvProperty) {
        this.machineEnvironment.add(machineEnvProperty);
        return this;
    }

    public MachineBuilder setConfigureMachineWithSwarm(boolean configureMachineWithSwarm) {
        this.configureMachineWithSwarm = configureMachineWithSwarm;
        return this;
    }

    public MachineBuilder setSwarmImage(String swarmImage) {
        this.swarmImage = swarmImage;
        return this;
    }

    public MachineBuilder setSwarmMaster(boolean swarmMaster) {
        this.swarmMaster = swarmMaster;
        return this;
    }

    public MachineBuilder setSwarmDiscovery(String swarmDiscovery) {
        this.swarmDiscovery = swarmDiscovery;
        return this;
    }

    public MachineBuilder setSwarmStrategy(String swarmStrategy) {
        this.swarmStrategy = swarmStrategy;
        return this;
    }

    public MachineBuilder setSwarmEnvironment(Set<String> swarmEnvironment) {
        this.swarmEnvironment = swarmEnvironment;
        return this;
    }

    public MachineBuilder setSwarmHostURI(URI swarmHostURI) {
        this.swarmHostURI = swarmHostURI;
        return this;
    }

    public MachineBuilder setSwarmHostURI(String swarmHostURI) throws URISyntaxException {
        return setSwarmHostURI(new URI(swarmHostURI));
    }

    public MachineBuilder setSwarmAdvertizeURI(URI swarmAdvertizeURI) {
        this.swarmAdvertizeURI = swarmAdvertizeURI;
        return this;
    }

    public MachineBuilder setSwarmAdvertizeURI(String swarmAdvertizeURI) throws URISyntaxException {
        return setSwarmAdvertizeURI(new URI(swarmAdvertizeURI));
    }

    /**
     * This method collects all data from the current request and calls {@code docker-machine} to create the instance.
     * @return a new machine instance. Please check the machine's status to evaluate if the creation command was
     * successful {@code status=MachineStatus.Running}.
     */
    public Machine build(){
        StringBuilder createCommand = new StringBuilder("docker-machine create ");
        if(driver!=null){
            createCommand.append("--driver ").append(driver).append(' ');
        }
        if(installURL!=null){
            createCommand.append("--engine-install-url ").append(installURL).append(' ');
        }
        for(String opt:engineOptions){
            createCommand.append("--engine-opt ").append(opt).append(' ');
        }
        for(String reg:insecureRegistries){
            createCommand.append("--engine-insecure-registry ").append(reg).append(' ');
        }
        for(String lbl:labels){
            createCommand.append("--engine-label ").append(lbl).append(' ');
        }
        if(storageDriver!=null){
            createCommand.append("--engine-storage-driver ").append(storageDriver).append(' ');
        }
        if(driver!=null){
            createCommand.append("--driver ").append(driver).append(' ');
        }
        if(driver!=null){
            createCommand.append("--driver ").append(driver).append(' ');
        }
        for(String env:machineEnvironment){
            createCommand.append("--engine-env ").append(env).append(' ');
        }
        if(configureMachineWithSwarm){
            createCommand.append("--swarm-image ");
            if(swarmImage!=null){
                createCommand.append("--driver ").append(swarmImage).append(' ');
            }
            if(swarmMaster){
                createCommand.append("--swarm-master ");
            }
            if(swarmDiscovery!=null){
                createCommand.append("--swarm-discovery ").append(swarmDiscovery).append(' ');
            }
            if(swarmStrategy!=null){
                createCommand.append("--swarm-strategy ").append(swarmStrategy).append(' ');
            }
            for(String env:swarmEnvironment){
                createCommand.append("--swarm-opt ").append(env).append(' ');
            }
            if(swarmHostURI!=null){
                createCommand.append("--swarm-host ").append(swarmHostURI).append(' ');
            }
            if(swarmAdvertizeURI!=null){
                createCommand.append("--swarm-addr ").append(swarmAdvertizeURI).append(' ');
            }
        }
        String command = createCommand.toString();
        LOG.info("Creating new machine with: " + command);
        LOG.info("MACHINE CREATION RESULT: " + Executor.execute(command));
        return Machines.getMachine(name);
    }
}
