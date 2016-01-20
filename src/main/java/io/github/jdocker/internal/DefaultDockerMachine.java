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

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import io.github.jdocker.JDockerMachine;
import io.github.jdocker.Machines;
import io.github.jdocker.common.Executor;
import io.github.jdocker.common.JSONMapper;
import io.github.jdocker.JDockerMachineException;
import io.github.jdocker.MachineConfig;
import io.github.jdocker.MachineStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by atsticks on 17.01.16.
 */
public final class DefaultDockerMachine implements JDockerMachine {

    private static final Logger LOG = Logger.getLogger(DefaultDockerMachine.class.getName());

    private DockerClient client;
    /** The current status. */
    private MachineStatus status = MachineStatus.NotExisting;
    /** The docker-machine's url/location. */
    private URI url;
    /** The number of CPUs. */
    private int cpus = 1;
    /** The docker-machine's memory. */
    private int memory;
    /** The docker-machine's disk size. */
    private long diskSize;

    private MachineConfig configuration;

    public String getName() {
        return configuration.getName();
    }

    /** The timestamp, when the docker-machine overall state was refreshed. */
    long lastUpdated = System.currentTimeMillis();

    /** The mapper for reading the inspection JSON. */
    private JSONMapper jsonMapper = new JSONMapper();

    public DefaultDockerMachine(MachineConfig config){
        this.configuration = config;
        refresh();
    }

    public int getCPUs(){
        return cpus;
    }

    public int getMemory(){
        return memory;
    }

    public long getDiskSize(){
        return diskSize;
    }

    public long getLastUpdated(){ return lastUpdated; }

    public MachineStatus getMachineStatus(){ return status; }

    public MachineConfig getConfiguration(){return configuration; }


    /**
     * <pre>
     * {
     "ConfigVersion": 3,
     "Driver": {
     "VBoxManager": {},
     "IPAddress": "192.168.99.103",
     "SSHUser": "docker",
     "SSHPort": 59187,
     "MachineName": "swarm-agent-00",
     "SwarmMaster": false,
     "SwarmHost": "tcp://0.0.0.0:3376",
     "SwarmDiscovery": "token://291c01c426135975e91d9f81b2a276df",
     "StorePath": "/home/atsticks/.docker/docker-machine",
     "CPU": 1,
     "Memory": 1024,
     "DiskSize": 20000,
     "Boot2DockerURL": "",
     "Boot2DockerImportVM": "",
     "HostOnlyCIDR": "192.168.99.1/24",
     "HostOnlyNicType": "82540EM",
     "HostOnlyPromiscMode": "deny",
     "NoShare": false
     },
     "DriverName": "virtualbox",
     "HostOptions": {
     "Driver": "",
     "Memory": 0,
     "Disk": 0,
     "EngineOptions": {
     "ArbitraryFlags": [],
     "Dns": null,
     "GraphDir": "",
     "Env": [],
     "Ipv6": false,
     "InsecureRegistry": [],
     "Labels": [],
     "LogLevel": "",
     "StorageDriver": "",
     "SelinuxEnabled": false,
     "TlsVerify": true,
     "RegistryMirror": [],
     "InstallURL": "https://get.docker.com"
     },
     "SwarmOptions": {
     "IsSwarm": true,
     "Address": "",
     "Discovery": "token://291c01c426135975e91d9f81b2a276df",
     "Master": false,
     "Host": "tcp://0.0.0.0:3376",
     "Image": "swarm:latest",
     "Strategy": "spread",
     "Heartbeat": 0,
     "Overcommit": 0,
     "ArbitraryFlags": []
     },
     "AuthOptions": {
     "CertDir": "/home/atsticks/.docker/docker-machine/certs",
     "CaCertPath": "/home/atsticks/.docker/docker-machine/certs/ca.pem",
     "CaPrivateKeyPath": "/home/atsticks/.docker/docker-machine/certs/ca-key.pem",
     "CaCertRemotePath": "",
     "ServerCertPath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00/server.pem",
     "ServerKeyPath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00/server-key.pem",
     "ClientKeyPath": "/home/atsticks/.docker/docker-machine/certs/key.pem",
     "ServerCertRemotePath": "",
     "ServerKeyRemotePath": "",
     "ClientCertPath": "/home/atsticks/.docker/docker-machine/certs/cert.pem",
     "StorePath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00"
     }
     },
     "Name": "swarm-agent-00",
     "RawDriver": "eyJWQm94TWFuYWdlciI6e30sIklQQWRkcmVzcyI6IjE5Mi4xNjguOTkuMTAzIiwiU1NIVXNlciI6ImRvY2tlciIsIlNTSFBvcnQiOjU5MTg3LCJNYWNoaW5lTmFtZSI6InN3YXJtLWFnZW50LTAwIiwiU3dhcm1NYXN0ZXIiOmZhbHNlLCJTd2FybUhvc3QiOiJ0Y3A6Ly8wLjAuMC4wOjMzNzYiLCJTd2FybURpc2NvdmVyeSI6InRva2VuOi8vMjkxYzAxYzQyNjEzNTk3NWU5MWQ5ZjgxYjJhMjc2ZGYiLCJTdG9yZVBhdGgiOiIvaG9tZS9hdHN0aWNrcy8uZG9ja2VyL21hY2hpbmUiLCJDUFUiOjEsIk1lbW9yeSI6MTAyNCwiRGlza1NpemUiOjIwMDAwLCJCb290MkRvY2tlclVSTCI6IiIsIkJvb3QyRG9ja2VySW1wb3J0Vk0iOiIiLCJIb3N0T25seUNJRFIiOiIxOTIuMTY4Ljk5LjEvMjQiLCJIb3N0T25seU5pY1R5cGUiOiI4MjU0MEVNIiwiSG9zdE9ubHlQcm9taXNjTW9kZSI6ImRlbnkiLCJOb1NoYXJlIjpmYWxzZX0="
     }
     * </pre>
     */
    public void refresh(){
        Map<String,String> data = inspect();
        if(data.get("Driver.Memory")!=null){
            memory = Integer.parseInt(data.get("Driver.Memory"));
        }
        else{
            memory = 0;
        }
        if(data.get("Driver.DiskSize")!=null) {
            diskSize = Long.parseLong(data.get("Driver.DiskSize"));
        }
        else{
            diskSize = 0;
        }
        refreshStatus();
        readEnvironment();
        refreshURL();
    }

    /**
     * Reloads the environment from DockerNodeRegistry.
     */
    public Set<String> readEnvironment(){
        Set<String> environment = new TreeSet<String>();
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(Executor.executeToInputStream("docker env " + getName())));
            String line = "";
            while ((line = reader.readLine())!= null) {
                if(line.startsWith("#")){
                    continue;
                }
                // export DOCKER_TLS_VERIFY="1"
                line = line.substring("export ".length());
                environment.add(line.trim());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error reading environment for docker-machine: " + getName(), e);
        }
        return environment;
    }

    /**
     * Inspect a docker-machine's details.
     * @return
     */
    public Map<String,String> inspect(){
        InputStream is = null;
        try {
            is = Executor.executeToInputStream("docker-machine inspect " + getName());
            return jsonMapper.readJsonData(is);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to inspect docker " + getName(), e);
        }
    }

    /**
     * Refresh the docker-machine's status only by calling {@code docker-docker-machine status <NAME>}.
     * @return the docker-machine's status
     */
    public MachineStatus refreshStatus(){
        try{
            String ret = Executor.execute("docker-machine status " + getName());
            if(("Host \""+getName()+"\" does not exist").equals(ret)){
                this.status = MachineStatus.NotExisting;
            }
            else{
                this.status = MachineStatus.valueOf(ret);
            }
        }
        catch(Exception e){
            this.status = MachineStatus.Unknown;
        }
        return status;
    }

    /**
     * Restarts a docker-machine.
     */
    public void restart(){
        String result = Executor.execute("docker-docker-machine restart " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to restart docker-machine " + getName() + " -> " + result);
        }
        this.status = MachineStatus.Running;
    }

    /**
     * Regenerates the TLS certificates.
     */
    public void regenerateCerts(){
        String result = Executor.execute("docker-docker-machine regenerate " + getName());
    }

    /**
     * Removes a docker-machine.
     */
    public void remove(){
        String result = Executor.execute("docker-docker-machine remove " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to kill docker-machine " + getName() + " -> " + result);
        }
        this.status = MachineStatus.NotExisting;
    }

    /**
     * Removes the docker-machine completely.
     */
    public void kill(){
        String result = Executor.execute("docker-docker-machine kill " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to kill docker-machine " + getName() + " -> " + result);
        }
        this.status = MachineStatus.Stopped;
    }

    /**
     * Starts the docker-machine.
     */
    public void start(){
        String result = Executor.execute("docker-docker-machine start " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to start docker-machine " + getName() + " -> " + result);
        }
        this.status = MachineStatus.Running;
    }

    /**
     * Stops the docker-machine.
     */
    public void stop(){
        String result = Executor.execute("docker-docker-machine stop " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to stop docker-machine " + getName() + " -> " + result);
        }
        this.status = MachineStatus.Stopped;
    }

    /**
     * Get the docker-machine^s URL.
     * @return
     */
    public URI getURL(){
        if(url==null){
            return refreshURL();
        }
        return url;
    }

    @Override
    public boolean ping() {
        // TODO implement ping
        return true;
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    /**
     * Refreshing the docker-machine's URL by calling {@code docker-machine url <name>}.
     * @return
     */
    public URI refreshURL(){
        try {
            this.url = new URI(Executor.execute("docker-machine url " + getName()));
            return this.url;
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Failed to evaluate docker-machine URL for " + getName(), e);
        }
    }

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    public void upgrade(){
        String result = Executor.execute("docker-machine upgrade " + getName());
        if(!result.isEmpty()){
            refreshStatus();
            throw new JDockerMachineException("Failed to upgrade docker-machine " + getName() + " -> " + result);
        }
    }

    /**
     * Get the UTC timestamp of the last full update of this docker-machine's state.
     * @return the last update in millis.
     */
    public long getLastUpdate(){
        return lastUpdated;
    }

    public DockerClient createDockerClient() {
        try {
            Map<String, String> machineData = inspect();
            return DefaultDockerClient.builder().dockerCertificates(
                    DockerCertificates.builder().dockerCertPath(
                            new File(machineData.get("HostOptions.AuthOptions.ServerCertPath")).toPath())
                            .caCertPath(
                                    new File(machineData.get("HostOptions.AuthOptions.CaCertPath")).toPath())
                            .clientCertPath(new File(machineData.get("HostOptions.AuthOptions.ClientCertPath")).toPath())
                            .clientKeyPath(new File(machineData.get("HostOptions.AuthOptions.ClientKeyPath")).toPath()).build().get())
                    .uri(getURL()).build();
        }
        catch(Exception e){
            throw new IllegalStateException("Failed to create Docker Client", e);
        }
    }

    /**
     * Registers this machine into the global shared Docker repository accessible from
     * {@link Machines}.
     */
    public JDockerMachine registerMachineAsDockerNode(String...labels){
        try {
            return Machines.addDocker(getName(), createDockerClient(), labels);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create DockerClient", e);
        }
    }

    public void createMachine(){
        if(configuration==null){
            throw new IllegalStateException("Cannot create machine without machine configuration.");
        }
        StringBuilder createCommand = new StringBuilder("docker-machine create ");
        if(this.configuration.getDriver()!=null){
            createCommand.append("--driver ").append(this.configuration.getDriver()).append(' ');
        }
        if(this.configuration.getInstallURL()!=null){
            createCommand.append("--engine-install-url ").append(this.configuration.getInstallURL()).append(' ');
        }
        for(String opt:this.configuration.getEngineOptions()){
            createCommand.append("--engine-opt ").append(opt).append(' ');
        }
        for(String reg:this.configuration.getInsecureRegistries()){
            createCommand.append("--engine-insecure-registry ").append(reg).append(' ');
        }
        for(String lbl:this.configuration.getLabels()){
            createCommand.append("--engine-label ").append(lbl).append(' ');
        }
        if(this.configuration.getStorageDriver()!=null){
            createCommand.append("--engine-storage-driver ").append(this.configuration.getStorageDriver()).append(' ');
        }
        for(String env:this.configuration.getMachineEnvironment()){
            createCommand.append("--engine-env ").append(env).append(' ');
        }
        if(this.configuration.isConfiguredWithSwarm()){

            if(this.configuration.getSwarmImage()!=null){
                createCommand.append("--swarm-image ").append(this.configuration.getSwarmImage()).append(' ');
            }
            if(this.configuration.isSwarmMaster()){
                createCommand.append("--swarm-master ");
            }
            if(this.configuration.getSwarmDiscoveryToken() !=null){
                createCommand.append("--swarm-discovery ").append(this.configuration.getSwarmDiscoveryToken()).append(' ');
            }
            if(this.configuration.getSwarmStrategy()!=null){
                createCommand.append("--swarm-strategy ").append(this.configuration.getSwarmStrategy()).append(' ');
            }
            for(String env:this.configuration.getSwarmEnvironment()){
                createCommand.append("--swarm-opt ").append(env).append(' ');
            }
            if(this.configuration.getSwarmHostURI()!=null){
                createCommand.append("--swarm-host ").append(this.configuration.getSwarmHostURI()).append(' ');
            }
            if(this.configuration.getSwarmAdvertizeURI()!=null){
                createCommand.append("--swarm-addr ").append(this.configuration.getSwarmAdvertizeURI()).append(' ');
            }
        }
        createCommand.append(' ').append(this.configuration.getName());
        String command = createCommand.toString();
        LOG.info("Creating new machine with: " + command);
        String result = Executor.execute(command);
        if(result.trim().isEmpty()){
            LOG.info("MACHINE " + this.configuration.getName() +" HAS BEEN CREATED.");
        }
        else{
            LOG.info("MACHINE CREATION PROBABLY FAILED: " + result);
        }
        refreshStatus();
    }

    @Override
    public String toString() {
        return "Machine{" +
                "lastUpdated=" + lastUpdated +
                ", diskSize=" + diskSize +
                ", memory=" + memory +
                ", cpus=" + cpus +
                ", url=" + url +
                ", status=" + status +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
