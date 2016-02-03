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
package io.github.jdocker.agent;

import io.github.jdocker.*;
import io.github.jdocker.common.ServiceContextManager;
import java.net.URI;
import java.util.*;

/**
 * Main Docker process, which is able to perform deployment, monitoring as well as statistical functions.
 */
public interface DockerAgent{

    /**
     * Get the unique agent's name.
     * @return the name, not null.
     */
    String getAgentName();

    /**
     * Get the unique agent's uri address.
     * @return the agent's uri, should be resolvable using DNS.
     */
    URI getURI();

    /**
     * Get the agent's current status.
     * @return the current status.
     */
    DockerAgentStatus getStatus();

    /**
     * Checks if docker is installed.
     * @return true if the command could be located
     */
    boolean isDockerInstalled();

    /**
     * Checks if docker-machine is installed.
     * @return true if the command could be located
     */
    boolean isDockerMachineInstalled();

    /**
     * Checks if docker-compose is installed.
     * @return true if the command could be located
     */
    boolean isDockerComposeInstalled();

    /**
     * Checks if the SDN provider is installed.
     * @return true if the command could be located
     */
    boolean isSDNInstalled();

    /**
     * Gets the agents labels.
     * @return the current labels.
     */
    Map<String,String> getLabels();

    /**
     * Sets the agents labels.
     * @param labels the labels
     */
    void setLabels(Map<String,String> labels);

    /**
     * Get the underlying kernel version.
     * @return the underlying kernel version.
     */
    String getKernelVersion();

    // command line commands
    /*
    Client:
 Version:      1.9.1
 API version:  1.21
 Go version:   go1.4.2
 Git commit:   a34a1d5
 Built:
 OS/Arch:      linux/amd64

Server:
 Version:      1.9.1
 API version:  1.21
 Go version:   go1.4.2
 Git commit:   a34a1d5
 Built:
 OS/Arch:      linux/amd6
     */
    String getDockerVersion();

    /*
    Containers: 34
Images: 222
Server Version: 1.9.1
Storage Driver: btrfs
 Build Version: Btrfs v4.3+20151116
 Library Version: 101
Execution Driver: native-0.2
Logging Driver: json-file
Kernel Version: 3.16.7-29-desktop
Operating System: openSUSE 13.2 (Harlequin) (x86_64)
CPUs: 8
Total Memory: 15.6 GiB
Name: workhorse.atsticks.ch
ID: Q3SM:6EYV:MFLM:BFWF:S6T4:WDL3:MONH:4NF5:MKXC:H2R6:ZMQZ:DJ4B
WARNING: No swap limit support
     */
    String getDockerInfo();

    /**
     * Access a machine status.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineStatus(String name);

    /**
     * Access a machine ip.
     * @param name the machine ip , not null.
     * @return the status, not null
     */
    String machineIP(String name);

    /**
     * Access a machine status.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineUpgrade(String name);

    /**
     * Starts a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineStart(String name);

    /**
     * Restarts a machine.
     * @param name the machine name , not null.
     * @return the result, not null
     */
    String machineRestart(String name);

    /**
     * Stops a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineStop(String name);

    /**
     * Kills a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineKill(String name);

    /**
     * Removes/deletes a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    String machineDelete(String name);

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    List<String> machineList();

    /**
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    String machineCreate(MachineConfig machineConfig);

    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    String machineInspect(String name);

    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    MachineConfig getMachineConfig(String name);

    /**
     * Access the singleton instance of the agent.
     * @return the agent instance.
     */
    static DockerAgent getInstance(){
        return ServiceContextManager.getServiceContext().getService(DockerAgent.class);
    }

}
