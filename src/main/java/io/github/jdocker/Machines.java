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

import io.github.jdocker.spi.MachinesSpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.*;

/**
 * Facade class mapping the most important commands of {@code docker-io.github.jdocker.machine}.
 */
public class Machines {

    private static final MachinesSpi SPI = ServiceContextManager.getServiceContext().getService(MachinesSpi.class);

    /** Singleton. */
    private Machines(){}

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public static List<String> getMachineNames(){
        return SPI.getMachineNames();
    }

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    public static List<DockerMachine> getKnownMachines(){
        return SPI.getKnownMachines();
    }

    /**
     * Access a machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    public static DockerMachine lookupMachine(String name){
        return SPI.lookupMachine(name);
    }

    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public static MachineConfig getMachineConfig(String name){
        return SPI.getMachineConfig(name);
    }


    /**
     * Stops all machines known that currently are still running.
     */
    public static void stopRunning(){
        SPI.stopRunning();
    }

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void stopRunning(String expression){
        SPI.stopRunning(expression);
    }

    /**
     * Starts all not running machines.
     */
    public static void startNotRunning(){
        SPI.startNotRunning();
    }

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void startNotRunning(String expression){
        SPI.startNotRunning(expression);
    }

    /**
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    public static DockerMachine createMachine(MachineConfig machineConfig) {
        return SPI.createMachine(machineConfig);
    }
}
