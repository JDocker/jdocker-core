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
import io.github.jdocker.spi.ServiceContextManager;

import java.util.Collection;
import java.util.Set;

/**
 * Registry managing the docker registries known to this master orchestration node.
 */
public class HostRegistry {

    private static final HostRegistrySpi SPI = ServiceContextManager.getServiceContext().getService(
            HostRegistrySpi.class
    );

    private HostRegistry(){}

    public static DockerMachine addDocker(String name, DockerClient client, String... labels){
        return SPI.addDocker(name, client, labels);
    }

    public static DockerMachine getDocker(String name){
        return SPI.getDocker(name);
    }

    public static Collection<DockerMachine> getDockers(){
        return SPI.getDockerMachines();
    }

    public static Collection<DockerMachine> getDockers(Predicate<DockerMachine> predicate){
        return SPI.getDockerMachines(predicate);
    }

    public static Set<String> getDockerNames(){
        return SPI.getDockerHostNames();
    }

    public static DockerMachine removeDocker(String name){
        return SPI.removeDockerHost(name);
    }

    /**
     * Add an ssh-accessible machine. to the pool of machines.
     * @param machine the machine, not null
     */
    public static void addMachine(Host machine){
        SPI.addHost(machine);
    }

    /**
     * Get a list with all currently known, but not used machines.
     * @return all unused machines.
     */
    public static Collection<Host> getMachines(){
        return SPI.getHosts();
    }

    /**
     * Get the machine instance for an ip address.
     * @param address the address, or resolvable dns name, not null.
     * @return the machine instance, or null, if the machine is not registered.
     */
    public static Host getMachine(String address){
        return SPI.getHost(address);
    }

}
