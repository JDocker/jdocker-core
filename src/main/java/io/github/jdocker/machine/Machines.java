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

    private static final Map<String,Machine> MACHINE_CACHE = new ConcurrentHashMap<String, Machine>();
    /** Singleton. */
    private Machines(){}

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public static List<String> getMachineNames(){
        String namesToParse = Executor.execute("docker-io.github.jdocker.machine ls");
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
    public static List<Machine> getMachinesInfo(){
        List<Machine> list = new ArrayList<Machine>();
        List<String> names = getMachineNames();
        for(String name: names){
            Machine machine = getMachine(name);
            if(machine!=null){
                list.add(machine);
            }
        }
        return list;
    }

    /**
     * Access a io.github.jdocker.machine by name.
     * @param name the io.github.jdocker.machine name , not null.
     * @return the io.github.jdocker.machine instance, or null.
     */
    public static Machine getMachine(String name){
        Machine machine = MACHINE_CACHE.get(name);
        if(machine==null){
            machine = new Machine(name);
            machine.refresh();
            MACHINE_CACHE.put(name, machine);
        }
        return machine;
    }

    /**
     * Creates a new builder for a machine. Building the machine will also call docker-machine to create the instance.
     * @param name the machine's name, not null.
     * @return the new builder.
     */
    public MachineBuilder createMachine(String name){
        return new MachineBuilder(name);
    }

    /**
     * Stops all machines known that currently are still running.
     */
    public static void stopRunning(){
        List<Machine> machines = getMachinesInfo();
        for(Machine machine:machines){
            if(machine.getStatus()==MachineStatus.Running){
                machine.stop();
            }
        }
    }

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void stopRunning(String expression){
        List<Machine> machines = getMachinesInfo();
        for(Machine machine:machines){
            if(machine.getStatus()==MachineStatus.Running && machine.getName().matches(expression)){
                machine.stop();
            }
        }
    }

    /**
     * Starts all not running machines.
     */
    public static void startNotRunning(){
        List<Machine> machines = getMachinesInfo();
        for(Machine machine:machines){
            if(machine.getStatus()!=MachineStatus.Running){
                machine.start();
            }
        }
    }

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    public static void startNotRunning(String expression){
        List<Machine> machines = getMachinesInfo();
        for(Machine machine:machines){
            if(machine.getStatus()!=MachineStatus.Running && machine.getName().matches(expression)){
                machine.start();
            }
        }
    }

    public static List<Machine> getMachines(Region region) {
        // TODO implement mapping of region to machines based on labels
        return Collections.emptyList();
    }

}
