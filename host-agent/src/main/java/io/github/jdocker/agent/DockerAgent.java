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
     * Access the singleton instance of the agent.
     * @return the agent instance.
     */
    static DockerAgent getInstance(){
        return ServiceContextManager.getServiceContext().getService(DockerAgent.class);
    }

}
