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
package io.github.djpaas.machine;

import io.github.djpaas.Executor;
import io.github.djpaas.JSONMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Model of a machine managed by docker machine.
 */
public class Machine {
    private static final Logger LOG = Logger.getLogger(Machine.class.getName());
    /** The machine's name. */
    private String name;
    /** The current status. */
    private MachineStatus status = MachineStatus.NotExisting;
    /** THe machine's url/location. */
    private URI url;
    /** The machine's access driver. */
    private String driver;
    /** THe number of CPUs. */
    private int cpus = 1;
    /** The machine's memory. */
    private int memory;
    /** The machine's disk size. */
    private long diskSize;
    /** The timestamp, when the machine overall state was refreshed. */
    private long lastUpdated = 0;
    /** The machine environment variables. */
    private Map<String,String> environment = new HashMap<String, String>();

    private int dockerPort;
    private String healthService;

    /** The mapper for reading the inspection JSON. */
    private JSONMapper jsonMapper = new JSONMapper();

    public Machine(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName(){
        return name;
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

    public String create(String driver, Map<String,String> params){
        return Executor.execute("docker-machine create --driver " + driver + " " + name);
    }

    /**
     * Access the machine's environment.
     * @return
     */
    public Map<String,String> getEnvironment(){
        return environment;
    }

    /**
     * Reloads the environment from Docker.
     */
    public Map<String,String> refreshEnvironment(){
        try {
            Map<String,String> environment = new HashMap<String, String>();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(Executor.executeToInputStream("docker env " + name)));
            String line = "";
            while ((line = reader.readLine())!= null) {
                if(line.startsWith("#")){
                    continue;
                }
                // export DOCKER_TLS_VERIFY="1"
                line = line.substring("export ".length());
                int index = line.indexOf('=');
                if(index>0){
                    String key = line.substring(0,index);
                    String value = line.substring(index+1, line.length()-2);
                    environment.put(key, value);
                }
            }
            this.environment = environment;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error reading environment for machine: " + name, e);
        }
        return this.environment;
    }

    /**
     * Inspect a machine's details.
     * @return
     */
    public Map<String,String> inspect(){
        InputStream is = null;
        try {
            is = Executor.executeToInputStream("docker-machine inspect " + name);
            return jsonMapper.readJsonData(is);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to inspect machine " + name, e);
        }
    }

    /**
     * <pre>
     *     {
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
     "StorePath": "/home/atsticks/.docker/machine",
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
     "CertDir": "/home/atsticks/.docker/machine/certs",
     "CaCertPath": "/home/atsticks/.docker/machine/certs/ca.pem",
     "CaPrivateKeyPath": "/home/atsticks/.docker/machine/certs/ca-key.pem",
     "CaCertRemotePath": "",
     "ServerCertPath": "/home/atsticks/.docker/machine/machines/swarm-agent-00/server.pem",
     "ServerKeyPath": "/home/atsticks/.docker/machine/machines/swarm-agent-00/server-key.pem",
     "ClientKeyPath": "/home/atsticks/.docker/machine/certs/key.pem",
     "ServerCertRemotePath": "",
     "ServerKeyRemotePath": "",
     "ClientCertPath": "/home/atsticks/.docker/machine/certs/cert.pem",
     "StorePath": "/home/atsticks/.docker/machine/machines/swarm-agent-00"
     }
     },
     "Name": "swarm-agent-00",
     "RawDriver": "eyJWQm94TWFuYWdlciI6e30sIklQQWRkcmVzcyI6IjE5Mi4xNjguOTkuMTAzIiwiU1NIVXNlciI6ImRvY2tlciIsIlNTSFBvcnQiOjU5MTg3LCJNYWNoaW5lTmFtZSI6InN3YXJtLWFnZW50LTAwIiwiU3dhcm1NYXN0ZXIiOmZhbHNlLCJTd2FybUhvc3QiOiJ0Y3A6Ly8wLjAuMC4wOjMzNzYiLCJTd2FybURpc2NvdmVyeSI6InRva2VuOi8vMjkxYzAxYzQyNjEzNTk3NWU5MWQ5ZjgxYjJhMjc2ZGYiLCJTdG9yZVBhdGgiOiIvaG9tZS9hdHN0aWNrcy8uZG9ja2VyL21hY2hpbmUiLCJDUFUiOjEsIk1lbW9yeSI6MTAyNCwiRGlza1NpemUiOjIwMDAwLCJCb290MkRvY2tlclVSTCI6IiIsIkJvb3QyRG9ja2VySW1wb3J0Vk0iOiIiLCJIb3N0T25seUNJRFIiOiIxOTIuMTY4Ljk5LjEvMjQiLCJIb3N0T25seU5pY1R5cGUiOiI4MjU0MEVNIiwiSG9zdE9ubHlQcm9taXNjTW9kZSI6ImRlbnkiLCJOb1NoYXJlIjpmYWxzZX0="
     }
     * </pre>
     */
    public Map<String,String> refresh(){
        Map<String,String> data = inspect();
        if(data.get("Driver.Memory")!=null){
            this.memory = Integer.parseInt(data.get("Driver.Memory"));
        }
        else{
            this.memory = 0;
        }
        if(data.get("Driver.DiskSize")!=null) {
            this.diskSize = Long.parseLong(data.get("Driver.DiskSize"));
        }
        else{
            this.diskSize = 0;
        }
        this.driver = data.get("Drivername");
        refreshStatus();
        refreshEnvironment();
        refreshURL();
        return data;
    }

    /**
     * Access the current machine status.
     * @return
     */
    public MachineStatus getStatus(){
        return status;
    }

    /**
     * Refresh the machine's status only by calling {@code docker-machine status <NAME>}.
     * @return the machine's status
     */
    public MachineStatus refreshStatus(){
        try{
            String ret = Executor.execute("docker-machine status " + name);
            if(("Host \""+name+"\" does not exist").equals(ret)){
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
     * Restarts a machine.
     */
    public void restart(){
        String result = Executor.execute("docker-machine restart " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to restart machine " + name + " -> " + result);
        }
        this.status = MachineStatus.Running;
    }

    /**
     * Regenerates the TLS certificates.
     */
    public void regenerateCerts(){
        String result = Executor.execute("docker-machine regenerate " + name);
    }

    /**
     * Removes a machine.
     */
    public void remove(){
        String result = Executor.execute("docker-machine remove " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to kill machine " + name + " -> " + result);
        }
        this.status = MachineStatus.NotExisting;
    }

    /**
     * Removes the machine completely.
     */
    public void kill(){
        String result = Executor.execute("docker-machine kill " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to kill machine " + name + " -> " + result);
        }
        this.status = MachineStatus.Stopped;
    }

    /**
     * Starts the machine.
     */
    public void start(){
        String result = Executor.execute("docker-machine start " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to start machine " + name + " -> " + result);
        }
        this.status = MachineStatus.Running;
    }

    /**
     * Stops the machine.
     */
    public void stop(){
        String result = Executor.execute("docker-machine stop " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to stop machine " + name + " -> " + result);
        }
        this.status = MachineStatus.Stopped;
    }

    /**
     * Get the machine^s URL.
     * @return
     */
    public URI getURL(){
        if(url==null){
            return refreshURL();
        }
        return url;
    }

    /**
     * Refreshing the machine's URL by calling {@code docker-machine url <name>}.
     * @return
     */
    public URI refreshURL(){
        try {
            return new URI(Executor.execute("docker-machine url " + name));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Failed to evaluate machine URL for " + name, e);
        }
    }

    /**
     * Upgrades the machine to the latest version of Docker.
     */
    public void upgrade(){
        String result = Executor.execute("docker-machine upgrade " + name);
        if(!result.isEmpty()){
            refreshStatus();
            throw new DockerMachineException("Failed to upgrade machine " + name + " -> " + result);
        }
    }

    /**
     * Get the UTC timestamp of the last full update of this machine's state.
     * @return the last update in millis.
     */
    public long getLastUpdate(){
        return lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Machine machine = (Machine) o;
        return name.equals(machine.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Machine{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", url=" + url +
                ", driver='" + driver + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
