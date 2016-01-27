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
package io.github.jdocker.spi;

import com.google.common.base.Predicate;
import com.spotify.docker.client.DockerClient;
import io.github.jdocker.JDockerHost;
import io.github.jdocker.UnpooledMachine;

import java.util.Collection;
import java.util.Set;

/**
 * Main component managing the currently known hosts in the system.
 */
public interface HostRegistrySpi {

    DockerHost addDocker(String name, DockerClient client, String... labels);

    DockerHost getDocker(String name);

    Collection<DockerHost> getDockerMachines(Predicate<DockerHost> predicate);

    Collection<DockerHost> getDockerMachines();

    Set<String> getDockerHostNames();

    DockerHost removeDockerHost(String name);

    void removeDockerHosts(Predicate<DockerHost> predicate);

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param host the machine, not null
     */
    void addHost(Machine host);

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    Collection<Machine> getHosts();

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    Machine getHost(String address);

    /**
     * Get current pool size;
     */
    int getPooledHostCount();

    /**
     * Get current pool size;
     */
    int getUnpooledHostCount();


}
