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
package io.github.jdocker.internal;

import io.github.jdocker.common.Executor;
import io.github.jdocker.JDockerHost;
import io.github.jdocker.MachineConfig;
import io.github.jdocker.MachineStatus;
import io.github.jdocker.spi.MachinesSpi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade class mapping the most important commands of {@code docker-io.github.jdocker.machine}.
 */
public class DefaultMachinesSpi implements MachinesSpi {

    private static final Logger LOG = Logger.getLogger(DefaultMachinesSpi.class.getName());

    private static final Map<String,MachineConfig> MACHINE_CONFIGS = new ConcurrentHashMap<>();

    private static final Map<String,JDockerHost> MACHINES = new ConcurrentHashMap<>();

    /** Singleton. */
    private DefaultMachinesSpi(){}

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    @Override
    public List<String> getMachineNames(){
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
    @Override
    public List<JDockerHost> getKnownMachines(){
        List<JDockerHost> list = new ArrayList<>();
        List<String> names = getMachineNames();
        for(String name: names){
            JDockerHost machine = lookupMachine(name);
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
    @Override
    public JDockerHost lookupMachine(String name){
        JDockerHost machine = MACHINES.get(name);
        if(machine==null){
            MachineConfig config = MACHINE_CONFIGS.get(name);
            if(config!=null) {
                machine = new DefaultDockerHost(config);
            }
            else{
                machine = new DefaultDockerHost(name);
                machine.refresh();
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
    @Override
    public MachineConfig getMachineConfig(String name){
        return MACHINE_CONFIGS.get(name);
    }


    /**
     * Stops all machines known that currently are still running.
     */
    @Override
    public void stopRunning(){
        List<JDockerHost> machines = getKnownMachines();
        for(JDockerHost machine:machines){
            if(machine.getMachineStatus()==MachineStatus.Running){
                machine.stop();
            }
        }
    }

    /**
     * Stops all running machines where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    @Override
    public void stopRunning(String expression){
        List<JDockerHost> machines = getKnownMachines();
        for(JDockerHost machine:machines){
            if(machine.getMachineStatus()==MachineStatus.Running && machine.getName().matches(expression)){
                machine.stop();
            }
        }
    }

    /**
     * Starts all not running machines.
     */
    @Override
    public void startNotRunning(){
        List<JDockerHost> machines = getKnownMachines();
        for(JDockerHost machine:machines){
            if(machine.getMachineStatus()!=MachineStatus.Running){
                machine.start();
            }
        }
    }

    /**
     * Starts all non running io.github.jdocker.machine where the given name expression matches.
     * @param expression the regular expresseion matched against the io.github.jdocker.machine name, not null.
     */
    @Override
    public void startNotRunning(String expression){
        List<JDockerHost> machines = getKnownMachines();
        for(JDockerHost machine:machines){
            if(machine.getMachineStatus()!=MachineStatus.Running && machine.getName().matches(expression)){
                machine.start();
            }
        }
    }

    @Override
    public JDockerHost createMachine(MachineConfig machineConfig) {
        JDockerHost machine = MACHINES.get(machineConfig.getName());
        if(machine!=null){
            LOG.warning("Cannot create machine: " +machineConfig.getName() + ": already exists!");
            return machine;
        }
        MachineConfig config = MACHINE_CONFIGS.get(machineConfig.getName());
        if(config!=null && config!=machineConfig){
            throw new IllegalStateException("Config already present for " + machineConfig.getName());
        }
        machine = new DefaultDockerHost(machineConfig);
        MACHINE_CONFIGS.put(machineConfig.getName(), machineConfig);
        MACHINES.put(machine.getName(), machine);
        machine.createMachine();
        return machine;
    }


}
