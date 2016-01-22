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

    JDockerHost addDocker(String name, DockerClient client, String... labels);

    JDockerHost getDocker(String name);

    Collection<JDockerHost> getDockerMachines(Predicate<JDockerHost> predicate);

    Collection<JDockerHost> getDockerMachines();

    Set<String> getDockerHostNames();

    JDockerHost removeDockerHost(String name);

    void removeDockerHosts(Predicate<JDockerHost> predicate);

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param host the machine, not null
     */
    void addHost(UnpooledMachine host);

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    Collection<UnpooledMachine> getHosts();

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    UnpooledMachine getHost(String address);

    /**
     * Get current pool size;
     */
    int getPooledHostCount();

    /**
     * Get current pool size;
     */
    int getUnpooledHostCount();


}
