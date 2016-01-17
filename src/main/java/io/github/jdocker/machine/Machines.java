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
package io.github.jdocker.machine;

import io.github.jdocker.common.Executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade class mapping the most important commands of {@code docker-io.github.jdocker.machine}.
 */
public class Machines {

    private static final Logger LOG = Logger.getLogger(Machines.class.getName());

    private static final Map<String,MachineConfig> MACHINE_CONFIGS = new ConcurrentHashMap<>();

    private static final Map<String,Machine> MACHINES = new ConcurrentHashMap<>();

    /** Singleton. */
    private Machines(){}

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public static List<String> getMachineNames(){
        String namesToParse = Executor.execute("docker-machine ls");
        BufferedReader reader = new BufferedReader(new StringReader(namesToParse));
        List<String> result = new ArrayList<>();
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Failed to read String..???", e);
        }
        boolean inSection = false;
        while(line!=null){
            if(inSection){
                // NAME   ACTIVE   DRIVER  STATE  URL  SWARM
                int index = line.indexOf(" ");
                result.add(line.substring(0,index));
            }
            if(line.startsWith("NAME")){
                inSection = true;
            }
            try {
                line = reader.readLine();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Failed to read String..???", e);
            }
        }
        return result;
    }

    /**
     * Get a list of all machines currently known. This will perform multiple requests to evaluate the
     * io.github.jdocker.machine state for every io.github.jdocker.machine name identified.
     * @return a list of io.github.jdocker.machine, refreshed.
     */
    public static List<Machine> getKnownMachines(){
        List<Machine> list = new ArrayList<>();
        List<String> names = getMachineNames();
        for(String name: names){
            Machine machine = lookupMachine(name);
            if(machine!=null){
                list.add(machine);
            }
        }
        return list;
    }

    /**
     * Access a machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    public static Machine lookupMachine(String name){
        Machine machine = MACHINES.get(name);
        if(machine==null){
            MachineConfig config = MACHINE_CONFIGS.get(name);
            if(config!=null) {
                machine = new Machine(config);
            }
            else{
                machine = new Machine(name);
            }
            MACHINES.put(name, machine);
        }else {
            machine.refresh();
        }
        return machine;
    }

    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public static MachineConfig getMachineConfig(String name){
        return MACHINE_CONFIGS.get(name);
    }


    /**
     * Stops all machines known that currently are still running.
     */
    public static void stopRunning(){
        List<Machine> machines = getKnownMachines();
        for(Machine machine:machines){
            if(machine.getMachineStatus()==MachineStatus.Running){
                machine.stop();
            }
        }
    }

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void stopRunning(String expression){
        List<Machine> machines = getKnownMachines();
        for(Machine machine:machines){
            if(machine.getMachineStatus()==MachineStatus.Running && machine.getName().matches(expression)){
                machine.stop();
            }
        }
    }

    /**
     * Starts all not running machines.
     */
    public static void startNotRunning(){
        List<Machine> machines = getKnownMachines();
        for(Machine machine:machines){
            if(machine.getMachineStatus()!=MachineStatus.Running){
                machine.start();
            }
        }
    }

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void startNotRunning(String expression){
        List<Machine> machines = getKnownMachines();
        for(Machine machine:machines){
            if(machine.getMachineStatus()!=MachineStatus.Running && machine.getName().matches(expression)){
                machine.start();
            }
        }
    }



}
