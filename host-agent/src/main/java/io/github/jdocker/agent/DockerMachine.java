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

import io.github.jdocker.MachineConfig;
import io.github.jdocker.common.ServiceContextManager;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Main Docker process, which is able to perform deployment, monitoring as well as statistical functions.
 */
public interface DockerMachine {

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
    static DockerMachine getInstance(){
        return ServiceContextManager.getServiceContext().getService(DockerMachine.class);
    }

}
