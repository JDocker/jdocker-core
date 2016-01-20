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


import com.google.common.base.Predicate;
import com.spotify.docker.client.DockerClient;
import io.github.jdocker.spi.HostRegistrySpi;
import io.github.jdocker.spi.MachinesSpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Registry managing the docker registries known to this master orchestration node.
 */
public final class Machines {

    private static final HostRegistrySpi SPI = ServiceContextManager.getServiceContext().getService(
            HostRegistrySpi.class
    );
    private static final MachinesSpi MACHINES_SPI = ServiceContextManager.getServiceContext().getService(MachinesSpi.class);

    private Machines(){}

    public static JDockerMachine addDocker(String name, DockerClient client, String... labels){
        return SPI.addDocker(name, client, labels);
    }

    public static JDockerMachine getDocker(String name){
        return SPI.getDocker(name);
    }

    public static Collection<JDockerMachine> getDockers(){
        return SPI.getDockerMachines();
    }

    public static Collection<JDockerMachine> getDockers(Predicate<JDockerMachine> predicate){
        return SPI.getDockerMachines(predicate);
    }

    public static Set<String> getDockerNames(){
        return SPI.getDockerHostNames();
    }

    public static JDockerMachine removeDocker(String name){
        return SPI.removeDockerHost(name);
    }

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param unpooledMachine the machine, not null
     */
    public static void addMachine(UnpooledMachine unpooledMachine){
        SPI.addHost(unpooledMachine);
    }

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    public static Collection<UnpooledMachine> getMachines(){
        return SPI.getHosts();
    }

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    public static UnpooledMachine getMachine(String address){
        return SPI.getHost(address);
    }

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public static List<String> getMachineNames(){
        return MACHINES_SPI.getMachineNames();
    }

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    public static List<JDockerMachine> getKnownMachines(){
        return MACHINES_SPI.getKnownMachines();
    }

    /**
     * Access a machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    public static JDockerMachine lookupMachine(String name){
        return MACHINES_SPI.lookupMachine(name);
    }

    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public static MachineConfig getMachineConfig(String name){
        return MACHINES_SPI.getMachineConfig(name);
    }

    /**
     * Restarts a docker-machine.
     */
    public static void restart(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Regenerates the TLS certificates.
     */
    public static void regenerateCerts(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Removes a docker-machine.
     */
    public static void remove(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Removes the docker-machine completely.
     */
    public static void kill(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Starts the docker-machine.
     */
    public static void start(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Stops the docker-machine.
     */
    public static void stop(JDockerMachine machine){
        // TODO delegate to SPI
    }

    /**
     * Get the docker-machine^s URL.
     * @return
     */
    public static URI getURL(JDockerMachine machine){
        // TODO delegate to SPI
        return null;
    }

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    public static void upgrade(JDockerMachine machine){
// TODO delegate to SPI
    }

    /**
     * Creates a client for accessing the docker host.
     * @return a client for accessing, not null
     */
    public static DockerClient createDockerClient(JDockerMachine machine){
        // TODO delegate to SPI
        return null;
    }

    /**
     * Creates the machine and configures it for being eligible as deployment target.
     */
    public static void createMachine(JDockerMachine machine){
        createMachine(machine.getConfiguration());
    }

    /**
     * Stops all machines known that currently are still running.
     */
    public static void stopAllMachines(){
        MACHINES_SPI.stopRunning();
    }

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void stopAllMachines(String expression){
        MACHINES_SPI.stopRunning(expression);
    }

    /**
     * Starts all not running machines.
     */
    public static void startNotRunningMachines(){
        MACHINES_SPI.startNotRunning();
    }

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void startNotRunningMachines(String expression){
        MACHINES_SPI.startNotRunning(expression);
    }

    /**
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    public static JDockerMachine createMachine(MachineConfig machineConfig) {
        return MACHINES_SPI.createMachine(machineConfig);
    }

    public static MachineStatus getMachineStatus(JDockerMachine machine) {
        // TODO delegate to SPI
        return MachineStatus.Unknown;
    }
}
