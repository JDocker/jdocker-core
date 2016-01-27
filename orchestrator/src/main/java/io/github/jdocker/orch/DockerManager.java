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


import com.spotify.docker.client.DockerClient;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Registry managing the docker registries known to this master orchestration node. Basically the DockerManager
 * implementation will sent according events to the docker agents listeningm which will perform the required action and
 * reply as useful.
 *
 * NOTE that in case where the Docker agent is installed within the hosting docker container killing the container will
 * also immedately delete the agent turning the machine into a unmanaged instance immedately. If the agent is installed
 * as a side process on the machine, it is even possible to uninstall and reinstallt or upgrade the docker container
 * running on the machine, so installing the agents along the docker container is the recommended way.
 */
public interface DockerManager {

    /**
     * Add a docker host from an existing docker installation. This will install the docker agent as a container
     * into the given docker instance.
     * @param name the new agent's symbolic name, not null.
     * @param client the configured client
     * @param labels any additional labels to be set for the agent.
     * @return the host instance, check the status
     */
    DockerHost registerDockerHost(String name, DockerClient client, String... labels);

    /**
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    DockerHost hostFromMachineConfig(MachineConfig machineConfig);

    /**
     * Create a new machine on the unmanaged instance given. This will establish a connection to the machine and
     * install the jdocker agent. The agent will determine if docker is already installed. If not it installs docker.
     * @param unmanagedInstance the unmanagedInstance instance, not null.
     * @return the new machine, check its status if all is OK.
     */
    DockerHost hostFromMachine(Machine unmanagedInstance);

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    List<DockerHost> getDockerHosts();

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    List<String> getDockerHostNames();

    /**
     * Removes docker from a given host, turning it into a unmanaged instance.
     * @param name the host name.
     * @return the new unmanaged machine instance represting the node.
     */
    void removeDockerHost(String name);

    /**
     * Access a machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    DockerHost lookupDockerHost(String name);

    /**
     * Access a machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    Machine lookupMachine(String name);

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param unpooledMachine the machine, not null
     */
    void addUnmanagedMachine(Machine unpooledMachine);

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    Collection<Machine> getUnmanagedMachines();

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    Machine getUnmanagedMachine(String address);



    /**
     * Restarts a docker-machine.
     */
    void restart(DockerHost machine);

    /**
     * Regenerates the TLS certificates.
     */
    void regenerateCerts(DockerHost machine);

    /**
     * Removes a docker-machine.
     */
    void remove(DockerHost machine);

    /**
     * Removes the docker-machine completely.
     */
    void kill(DockerHost machine);

    /**
     * Starts the docker-machine.
     */
    void start(DockerHost machine);

    /**
     * Stops the docker-machine.
     */
    void stop(DockerHost machine);

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    void upgrade(DockerHost machine);

    /**
     * Inspect a docker-machine's details.
     * @return the property mapped JSON tree response.
     */
    Map<String,String> inspect(DockerHost machine);

    /**
     * Get the underlying Machine configuration.
     * @return the full Machine configuration.
     */
    MachineConfig getConfiguration(DockerHost machine);

    /**
     * Creates a client for accessing the docker host using the docker container^s REST API.
     * @return a client for accessing, not null
     */
    DockerClient createDockerClient(DockerHost machine);

    /**
     * Stops all machines known that currently are still running.
     */
    void stopAllDockerServices();

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    void stopAllDockerServices(String expression);

    /**
     * Starts all not running machines.
     */
    void startNotRunningDockers();

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    void startNotRunningDockers(String expression);

}
