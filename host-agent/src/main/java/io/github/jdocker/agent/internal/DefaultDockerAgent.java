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
