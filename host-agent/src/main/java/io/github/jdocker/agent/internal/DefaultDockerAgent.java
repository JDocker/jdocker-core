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
public class DefaultDockerAgent implements DockerAgent{

    private static final Logger LOG = Logger.getLogger(DefaultDockerAgent.class.getName());

    /** Default interval that defines how often an agent sends its heatbeat data. */
    private static final long DEFAULT_HEARTBEAT_INTERVAL_MS = 30000L; // all 30 seconds
    /** The current node's machine config. */
    private MachineConfig machineConfig;
    /** The id of zhe timer for sending the heartbeats. */
    private long timerID;

    private io.github.jdocker.agent.DockerAgentStatus status = io.github.jdocker.agent.DockerAgentStatus.Running;

    public DefaultDockerAgent(){
        MachineConfigBuilder builder = null;
        try{
            InetAddress address = InetAddress.getLocalHost();
            LOG.info("Initializing JDocker Agent for " + address.getHostName());
            builder = MachineConfig.builder(address.getHostName());
            builder.setInstallUri("tcp:/"+address.getHostAddress());
            builder = MachineConfig.builder(address.getHostName());
        } catch (UnknownHostException e) {
            builder = MachineConfig.builder(UUID.randomUUID().toString());
        }

        builder.setDriver("direct-access");
        machineConfig = builder.build();
        LOG.info("Loaded JDocker Machine Configuration: " + machineConfig);
    }

    @Override
    public String getAgentName() {
        return this.machineConfig.getName();
    }

    @Override
    public URI getURI() {
        return null; // TODO
    }

    public io.github.jdocker.agent.DockerAgentStatus getStatus(){
        return status;
    }

    public Map<String,String> getLabels(){
        return this.machineConfig.getLabels();
    }

    public void setLabels(Map<String,String> labels){
        this.machineConfig.addLabels(labels);
    }

    public String getKernelVersion(){
        return Executor.execute("uname -r");
    }

    public String getCommand(String commandName){
        String[] locations = new String[]{System.getProperty("user.home")+"/bin/", "/usr/local/bin/","/usr/bin/","/bin/", "/usr/bin/X11/",
            "/usr/games/", "/opt/kde3/bin/"};
        for(String loc:locations){
            String filePath = loc + commandName;
            if(new File(filePath).exists()){
                return filePath;
            }
        }
        return null;
    }

    public boolean isWhichInstalled(){
        return getCommand("which")!=null;
    }

    public boolean isDockerInstalled(){
        return getCommand("docker")!=null;
    }

    public String installDocker()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Docker.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }

    @Override
    public boolean isSDNInstalled(){
        return getCommand("calico")!=null;
    }

    public String installSDN()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Calico.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }

    public boolean isSshInstalled(){
        return getCommand("ssh")!=null;
    }

    public boolean isDockerMachineInstalled(){
        return getCommand("docker-machine")!=null;
    }

    public String installDockerMachine()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Docker-Machine.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }

    public boolean isDockerComposeInstalled(){
        return getCommand("docker-compose")!=null;
    }

    public String installDockerCompose()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Docker-Compose.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }



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
    public String getDockerVersion(){
        return Executor.execute("docker version");
    }

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
    public String getDockerInfo(){
        return Executor.execute("docker info");
    }

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
        return null;
    }

    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public List<String> machineLS(){
        if(!isDockerMachineInstalled()){
            return Collections.emptyList();
        }
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



    /*
    docker0   Link encap:Ethernet  Hardware Adresse 02:42:40:5C:98:84
          inet Adresse:172.17.0.1  Bcast:0.0.0.0  Maske:255.255.0.0
          inet6 Adresse: fe80::42:40ff:fe5c:9884/64 Gültigkeitsbereich:Verbindung
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:679 errors:0 dropped:0 overruns:0 frame:0
          TX packets:821 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 Sendewarteschlangenlänge:0
          RX bytes:1967810 (1.8 Mb)  TX bytes:149518 (146.0 Kb)

eno1      Link encap:Ethernet  Hardware Adresse A0:B3:CC:4E:46:AE
          inet Adresse:192.168.1.4  Bcast:192.168.1.255  Maske:255.255.255.0
          inet6 Adresse: fe80::a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Verbindung
          inet6 Adresse: 2002:544a:d455:e472:a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Global
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:939691 errors:0 dropped:0 overruns:0 frame:0
          TX packets:571251 errors:0 dropped:9 overruns:0 carrier:0
          collisions:0 Sendewarteschlangenlänge:1000
          RX bytes:986884862 (941.1 Mb)  TX bytes:92269450 (87.9 Mb)

lo        Link encap:Lokale Schleife
          inet Adresse:127.0.0.1  Maske:255.0.0.0
          inet6 Adresse: ::1/128 Gültigkeitsbereich:Maschine
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:802403 errors:0 dropped:0 overruns:0 frame:0
          TX packets:802403 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 Sendewarteschlangenlänge:0
          RX bytes:198349659 (189.1 Mb)  TX bytes:198349659 (189.1 Mb)

vboxnet0  Link encap:Ethernet  Hardware Adresse 0A:00:27:00:00:00
          UP BROADCAST MULTICAST  MTU:1500  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 Sendewarteschlangenlänge:1000
          RX bytes:0 (0.0 b)  TX bytes:0 (0.0 b)
     */
    public String getIFConfig(){
        return Executor.execute("sudo ifconfig");
        // Look for
//        inet Adresse:192.168.1.4  Bcast:192.168.1.255  Maske:255.255.255.0
//        inet6 Adresse: fe80::a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Verbindung
//        inet6 Adresse: 2002:544a:d455:e472:a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Global
    }


    /**
     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
     * @return a list with all io.github.jdocker.machine names, never null.
     */
    public List<String> getMachineNames(){
        // Lookup in config clusters
        List<String> machines = new ArrayList<>();
        Configuration cfg = ConfigurationProvider.getConfiguration();
        String namesVal = cfg.get("jdocker.machines");
        if(namesVal!=null){
            machines.addAll(Arrays.asList(namesVal.split(",")));
        }
        machines.add(this.machineConfig.getName());
        return machines;
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


    private String[] loadScripts(String... urls)throws IOException{
        List<String> result = new ArrayList<>();
        for(String urlVal:urls){
            URL url = new URL(urlVal);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder b = new StringBuilder();
            String line = reader.readLine();
            while(line!=null){
                b.append(line);
            }
            result.add(b.toString());
        }
        return result.toArray(new String[result.size()]);
    }

}
