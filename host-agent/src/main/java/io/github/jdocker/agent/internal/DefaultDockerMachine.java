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
package io.github.jdocker.agent.internal;

import io.github.jdocker.Machine;
import io.github.jdocker.MachineConfig;
import io.github.jdocker.MachineConfigBuilder;
import io.github.jdocker.SwarmConfig;
import io.github.jdocker.agent.DockerAgent;
import io.github.jdocker.agent.DockerMachine;
import io.github.jdocker.common.Executor;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Docker process, which is able to perform deployment, monitoring as well as statistical functions.
 */
public class DefaultDockerMachine implements DockerMachine{

    private static final Logger LOG = Logger.getLogger(DefaultDockerMachine.class.getName());

    /**
     * Access a machine status.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    public String machineStatus(String name){
        return Executor.execute("docker-machine status " +name);
    }

    /**
     * Access a machine ip.
     * @param name the machine ip , not null.
     * @return the status, not null
     */
    public String machineIP(String name){
        return Executor.execute("docker-machine ip " +name);
    }

    /**
     * Access a machine status.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    public String machineUpgrade(String name){
        return Executor.execute("docker-machine upgrade " +name);
    }

    /**
     * Starts a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    public String machineStart(String name){
        return Executor.execute("docker-machine start " +name);
    }

    /**
     * Restarts a machine.
     * @param name the machine name , not null.
     * @return the result, not null
     */
    public String machineRestart(String name){
        return Executor.execute("docker-machine restart " +name);
    }

    /**
     * Stops a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    public String machineStop(String name){
        return Executor.execute("docker-machine stop " +name);
    }

    /**
     * Kills a machine.
     * @param name the machine name , not null.
     * @return the status, not null
     */
    public String machineKill(String name){
        return Executor.execute("docker-machine kill " +name);
    }

    @Override
    public String machineDelete(String name) {
        return Executor.execute("docker-machine rm " +name);
    }

    @Override
    public List<String> machineList() {
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
            if(inSection && !line.contains("error") && !line.contains("ERROR:")){
                // NAME   ACTIVE   DRIVER  STATE  URL  SWARM
                int index = line.indexOf(" ");
                if(index>0) {
                    result.add(line.substring(0, index));
                }
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
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    public String machineCreate(MachineConfig machineConfig){
        if(machineConfig==null){
            throw new IllegalStateException("Cannot create machine without machine configuration.");
        }
        StringBuilder createCommand = new StringBuilder("docker-machine create ");
        if(machineConfig.getDriver()!=null){
            createCommand.append("--driver ").append(machineConfig.getDriver()).append(' ');
        }
        if(machineConfig.getInstallURL()!=null){
            createCommand.append("--engine-install-url ").append(machineConfig.getInstallURL()).append(' ');
        }
        for(String opt:machineConfig.getEngineOptions()){
            createCommand.append("--engine-opt ").append(opt).append(' ');
        }
        for(String reg:machineConfig.getInsecureRegistries()){
            createCommand.append("--engine-insecure-registry ").append(reg).append(' ');
        }
        for(Map.Entry<String,String> en:machineConfig.getLabels().entrySet()){
            if(en.getKey().equals(en.getValue())){
                createCommand.append("--engine-label ").append(en.getValue()).append(' ');
            }
            else{
                createCommand.append("--engine-label ").append(en.getKey()+'='+en.getValue()).append(' ');
            }
        }
        if(machineConfig.getStorageDriver()!=null){
            createCommand.append("--engine-storage-driver ").append(machineConfig.getStorageDriver()).append(' ');
        }
        for(String env:machineConfig.getMachineEnvironment()){
            createCommand.append("--engine-env ").append(env).append(' ');
        }
        // OPtional: swarm config
        if(machineConfig.getSwarmConfig()!=null){
            SwarmConfig swarmConfig = machineConfig.getSwarmConfig();
            if(swarmConfig.getSwarmImage()!=null){
                createCommand.append("--swarm-image ").append(swarmConfig.getSwarmImage()).append(' ');
            }
            if(swarmConfig.isSwarmMaster()){
                createCommand.append("--swarm-master ");
            }
            if(swarmConfig.getSwarmDiscoveryToken() !=null){
                createCommand.append("--swarm-discovery ").append(swarmConfig.getSwarmDiscoveryToken()).append(' ');
            }
            if(swarmConfig.getSwarmStrategy()!=null){
                createCommand.append("--swarm-strategy ").append(swarmConfig.getSwarmStrategy()).append(' ');
            }
            for(String env:swarmConfig.getSwarmEnvironment()){
                createCommand.append("--swarm-opt ").append(env).append(' ');
            }
            if(swarmConfig.getSwarmHostURI()!=null){
                createCommand.append("--swarm-host ").append(swarmConfig.getSwarmHostURI()).append(' ');
            }
            if(swarmConfig.getSwarmAdvertizeURI()!=null){
                createCommand.append("--swarm-addr ").append(swarmConfig.getSwarmAdvertizeURI()).append(' ');
            }
        }
        createCommand.append(' ').append(machineConfig.getName());
        String command = createCommand.toString();
        LOG.info("Creating new machine with: " + command);
        String result = Executor.execute(command);
        if(result.trim().isEmpty()){
            LOG.info("MACHINE " + machineConfig.getName() +" HAS BEEN CREATED.");
        }
        else{
            LOG.info("MACHINE CREATION PROBABLY FAILED: " + result);
        }
        return Executor.execute(command);
    }



    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public String machineInspect(String name){
        return Executor.execute("docker-machine inspect " + name);
    }


    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public MachineConfig getMachineConfig(String name){
        Configuration cfg = ConfigurationProvider.getConfiguration();
        String config = cfg.get("jdocker.machine."+name);
        if(config!=null){
            return Machine.from(config);
        }
        return null;
    }

}
