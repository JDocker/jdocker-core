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
package io.github.jdocker.orch;

import io.github.jdocker.*;
import io.vertx.core.AbstractVerticle;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry managing the docker instances across the whole cluster. This instance listens to the rgular helath
 * status updated from the agents as well as the emergency messages, when things break. This functionality shouls also
 * be exposed to the outside world as REST services.
 */
public final class DockerManager extends AbstractVerticle {

    private static final Map<String,DockerAgentStatus> SPI = new ConcurrentHashMap<>();

//    public static DockerHost addDockerHost(String name, DockerClient client, String... labels){
//        return SPI.addDocker(name, client, labels);
//    }

    public DockerAgentStatus getDockerHost(String name){
        return null;
    }

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    public Collection<DockerAgentStatus> getDockerHosts(){
//        return MACHINES_SPI.getKnownMachines();
        return Collections.emptyList();
    }

    public DockerHost removeDockerHost(String name, boolean killIt){
//        return SPI.removeDockerHost(name);
        return null;
    }

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param unpooledMachine the machine, not null
     */
    public void addUnpooledMachine(Machine unpooledMachine){
//        SPI.addHost(unpooledMachine);
    }

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    public Collection<Machine> getUnpooledMachines(){
//        return SPI.getHosts();
        return Collections.emptyList();
    }

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    public static Machine getUnpooledMachine(String address){
//        return SPI.getHost(address);
        return null;
    }

    /**
     * Inspect a docker-machine's details.
     * @return the property mapped JSON tree response.
     */
    public static Map<String,String> inspect(String name){
        // TODO delegate to SPI
        return null;
    }

    /**
     * Restarts a docker-machine.
     */
    public static void restart(String name){
        // TODO delegate to SPI
    }

    /**
     * Regenerates the TLS certificates.
     */
    public static void regenerateCerts(String name){
        // TODO delegate to SPI
    }

    /**
     * Removes a docker-machine.
     */
    public void remove(String name){
        // TODO delegate to SPI
    }

    /**
     * Removes the docker-machine completely.
     */
    public void kill(String name){
        // TODO delegate to SPI
    }

    /**
     * Starts the docker-machine.
     */
    public void start(DockerHost machine){
        // TODO delegate to SPI
    }

    /**
     * Stops the docker-machine.
     */
    public void stop(DockerHost machine){
        // TODO delegate to SPI
    }

    /**
     * Get the docker-machine^s URL.
     * @return
     */
    public URI getURL(DockerHost machine){
        // TODO delegate to SPI
        return null;
    }

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    public void upgrade(DockerHost machine){
// TODO delegate to SPI
    }

//    /**
//     * Creates a client for accessing the docker host.
//     * @return a client for accessing, not null
//     */
//    public DockerClient createDockerClient(DockerHost machine){
//        // TODO delegate to SPI
//        return null;
//    }

    /**
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    public DockerHost createMachine(MachineConfig machineConfig) {
//        return MACHINES_SPI.createMachine(machineConfig);
        return null;
    }

    public MachineStatus getMachineStatus(DockerHost machine) {
        // TODO delegate to SPI
        return MachineStatus.Unknown;
    }
}
