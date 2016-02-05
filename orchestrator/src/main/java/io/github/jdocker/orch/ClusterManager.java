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

import com.spotify.docker.client.messages.ContainerInfo;
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
public interface ClusterManager {

    DockerHost getDockerHost(String name);

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    Collection<DockerHost> getDockerHosts();

    /**
     * Removes the given docker host from the cluster.
     * @param host the host.
     */
    void removeDockerHost(DockerHost host);

    /**
     * Adds the unpooled machine to the cluster. This will also install docker and the jdocker agent on the machine.
     * @param machine the machine
     * @param creadentials the credentials to secure login onto the machine.
     */
    void joinUnpooledMachine(Machine machine, Map<String,String> creadentials);

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param unpooledMachine the machine, not null
     */
    void addUnpooledMachine(Machine unpooledMachine);

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    Collection<Machine> getUnpooledMachines();

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    Machine getUnpooledMachine(String address);


    void deployComtainer(Deployment deployment);

    void undeployContains(ContainerInfo containerId);

}
