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

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Interface representing a docker container host.
 */
public interface JDockerHost extends UnpooledMachine {

    /**
     * Get the global machine name.
     *
     * @return the global machine name, not null.
     */
    String getName();

    /**
     * Get the number of CPUs of the machine.
     * @return the number of CPUs.
     */
    int getCPUs();

    /**
     * Get the memory available.
     * @return the memory on the machine.
     */
    int getMemory();

    /**
     * Get the container's disk size.
     * @return the container's disk size.
     */
    long getDiskSize();

    /**
     * Get the underlying Machine configuration.
     * @return the full Machine configuration.
     */
    MachineConfig getConfiguration();

    /**
     * Get the current machine status.
     * @return the machine's status.
     */
    default MachineStatus getMachineStatus(){
        return Machines.getMachineStatus(this);
    }

    /**
     * Get the machine's simple name, similar to a class name without package name part.
     * @return the simple name, not null.
     */
    default String getSimpleName(){
        int index = getName().lastIndexOf('.');
        if(index<0){
            return getName();
        }
        return getName().substring(index+1);
    }

    /**
     * Get the package name of the machine.
     * @return the package name, or an empty String, but never null.
     */
    default String getPacakge(){
        int index = getName().lastIndexOf('.');
        if(index<0){
            return "";
        }
        return getName().substring(0,index);
    }

    /**
     * Restarts a docker-machine.
     */
    default void restart(){
        Machines.restart(this);
    }

    /**
     * Regenerates the TLS certificates.
     */
    default void regenerateCerts(){
        Machines.regenerateCerts(this);
    }

    /**
     * Removes a docker-machine.
     */
    default void remove(){
        Machines.remove(this);
    }

    /**
     * Removes the docker-machine completely.
     */
    default void kill(){
        Machines.kill(this);
    }

    /**
     * Starts the docker-machine.
     */
    default void start(){
        Machines.start(this);
    }

    /**
     * Stops the docker-machine.
     */
    default void stop(){
        Machines.stop(this);
    }

    /**
     * Get the docker-machine^s URL.
     * @return
     */
    default URI getURI(){
        return Machines.getURL(this);
    }

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    default void upgrade(){
        Machines.upgrade(this);
    }

    /**
     * Creates a client for accessing the docker host.
     * @return a client for accessing, not null
     */
    default DockerClient createDockerClient(){
        return Machines.createDockerClient(this);
    }

    /**
     * Creates the machine and configures it for being eligible as deployment target.
     */
    default void createMachine(){
        Machines.createMachine(this);
    }

}
