package io.github.jdocker.agent.internal;///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
//package io.github.jdocker.agent;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.jdocker.*;
//import io.github.jdocker.common.Executor;
//import io.github.jdocker.common.OwnedResult;
//import io.vertx.core.*;
//import io.vertx.core.eventbus.EventBus;
//import io.vertx.core.json.JsonObject;
//import org.apache.tamaya.Configuration;
//import org.apache.tamaya.ConfigurationProvider;
//
//import java.io.*;
//import java.net.InetAddress;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * Main Docker process, which is able to perform deployment, monitoring as well as statistical functions.
// */
//public class DockerAgentServiceImpl implements DockerAgentService {
//
//    private static final Logger LOG = Logger.getLogger(DockerAgentServiceImpl.class.getName());
//
//    /** Default interval that defines how often an agent sends its heatbeat data. */
//    private static final long DEFAULT_HEARTBEAT_INTERVAL_MS = 30000L; // all 30 seconds
//    /** The current node's machine config. */
//    private MachineConfig machineConfig;
//    /** The id of zhe timer for sending the heartbeats. */
//    private long timerID;
//
//    private Vertx vertx;
//
//    private ObjectMapper jsonMapper = new ObjectMapper();
//
//    public DockerAgentServiceImpl(Vertx vertx){
//        this.vertx = Objects.requireNonNull(vertx);
//        MachineConfigBuilder builder = null;
//        try{
//            InetAddress address = InetAddress.getLocalHost();
//            LOG.info("Initializing JDocker Agent for " + address.getHostName());
//            builder = MachineConfig.builder(address.getHostName());
//            builder.setInstallUri("tcp:/"+address.getHostAddress());
//            builder = MachineConfig.builder(address.getHostName());
//        } catch (UnknownHostException e) {
//            builder = MachineConfig.builder(UUID.randomUUID().toString());
//        }
//
//        builder.setDriver("direct-access");
//        machineConfig = builder.build();
//        LOG.info("Loaded JDocker Machine Configuration: " + machineConfig);
////        // register node into known nodes
////        EventBus eb = vertx.eventBus();
////        // TODO access a clustered event bus here...
////
////        eb.consumer(DockerAgentCommands.DOCKER_INSPECT.getName(),message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            message.reply(getMachineConfig(host).toJSON());
////        });
////        eb.consumer(DockerAgentCommands.AGENT_GET_LABELS.getName(), message -> {
////            message.reply(getLabels());
////        });
////        eb.consumer(DockerAgentCommands.AGENT_SET_LABELS.getName(), message -> {
////            Map<String,String> labels = (Map<String,String>)message.body();
////            this.machineConfig.addLabels(labels);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_VERSION.getName(), message -> {
////            message.reply(getDockerVersion());
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_INFO.getName(), message -> {
////            message.reply(getDockerInfo());
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_INSPECT.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            message.reply(machineInspect(host));
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_STATUS.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            message.reply(machineStatus(host));
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_STOP.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineStop(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_START.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineStart(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_UPGRADE.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineUpgrade(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_RESTART.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineRestart(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_KILL.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineKill(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_IP.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            machineIP(host);
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_DEPLOY.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
////            vertx.executeBlocking(future -> {
////                // Call some blocking API that takes a significant amount of time to return
////                //            if(host.equals(this.getAgentName())){
//////                // TODO
//////                deployLocally(message.getBody());
//////            }
//////            else{
//////                machineDeploy(host, message.getBody());
//////            }
////                future.complete(true);
////            }, res -> {
////                System.out.println("The result is: " + res.result());
////            });
////
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_CHECK_DEPLOYMENT.getName(), message -> {
////            String host = message.headers().get("host");
////            if(host==null){
////                message.fail(1, "host header missing.");
////            }
//////            CHeck if this installation (container) matches these settings here...
////            // and if the health state is OK for a deployment...
////        });
////        eb.consumer(DockerAgentCommands.DOCKER_SOFTWARE_CHECK.getName(), message -> {
////            message.reply(new JsonObject()
////                    .put("which", isWhichInstalled())
////                    .put("calico", isCalicoInstalled())
////                    .put("ssh", isSshInstalled())
////                    .put("docker-machine", isCalicoInstalled())
////                    .put("docker", isDockerInstalled()));
////        });
////        eb.send("jdocker.Docker.Agent:started", machineConfig.getName());
//        Long heartbeatInternal = ConfigurationProvider.getConfiguration().get("jdocker.Docker.Agent.heartbeatInterval", Long.class);
//        if(heartbeatInternal==null){
//            heartbeatInternal = Long.valueOf(DEFAULT_HEARTBEAT_INTERVAL_MS);
//        }
//        timerID = vertx.setTimer(heartbeatInternal, id -> {
//            // register node into known nodes
//            EventBus bus = vertx.eventBus();
//            bus.send("jdocker.DockerAgent:heartbeat", createHeartbeat());
//        });
//    }
//
//    public String getAgentName(){
//        return this.machineConfig.getName();
//    }
//
//    private JsonObject createHeartbeat(){
//        return createBaseJsonResponse("heart-beat").put("heart-beat", System.currentTimeMillis());
//    }
//
//    @Override
//    public void getLabels(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)){
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(), "labels",
//                    this.machineConfig.getLabels())));
//        }
//    }
//
//    @Override
//    public void setLabels(String agentName, Map<String,String> labels, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)){
//            this.machineConfig.addLabels(labels);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(), "labels",
//                    this.machineConfig.getLabels())));
//        }
//    }
//
//    @Override
//    public void getKernelVersion(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)){
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(), "kernel-version",Executor.execute("uname -r"))));
//        }
//    }
//
//    @Override
//    public void locateCommand(String agentName, String commandName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String[] locations = new String[]{System.getProperty("user.home") + "/bin/", "/usr/local/bin/", "/usr/bin/", "/bin/", "/usr/bin/X11/",
//                    "/usr/games/", "/opt/kde3/bin/"};
//            for (String loc : locations) {
//                String filePath = loc + commandName;
//                if (new File(filePath).exists()) {
//                    handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(),
//                            "locate-command:"+commandName, filePath)));
//                    return;
//                }
//            }
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(),
//                    "locate-command:"+commandName, "NOT FOUND.")));
//        }
//    }
//
////    @Override
////    public void isWhichInstalled(String host, Handler<AsyncResult<OwnedResult>> handler){
////        return getCommand("which")!=null;
////    }
//
//    @Override
//    public void isDockerInstalled(String host, Handler<AsyncResult<OwnedResult>> handler){
//        locateCommand(host, "docker", handler);
//    }
//
////    @Override
////    public void installDocker(String host, Handler<AsyncResult<OwnedResult>> handler){
////        // TODO call theforeman.org server
////    }
//
//    @Override
//    public void isDockerMachineInstalled(String host, Handler<AsyncResult<OwnedResult>> handler){
//        locateCommand(host, "docker-machine", handler);
//    }
//
//    @Override
//    public void isDockerComposeInstalled(String host, Handler<AsyncResult<OwnedResult>> handler){
//        locateCommand(host, "docker-compose", handler);
//    }
//
//    // command line commands
//    /*
//    Client:
// Version:      1.9.1
// API version:  1.21
// Go version:   go1.4.2
// Git commit:   a34a1d5
// Built:
// OS/Arch:      linux/amd64
//
//Server:
// Version:      1.9.1
// API version:  1.21
// Go version:   go1.4.2
// Git commit:   a34a1d5
// Built:
// OS/Arch:      linux/amd6
//     */
//    @Override
//    public void getDockerVersion(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker version");
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:"+getAgentName(),
//                    "docker:version:", result)));
//        }
//    }
//
//    /*
//    Containers: 34
//Images: 222
//Server Version: 1.9.1
//Storage Driver: btrfs
// Build Version: Btrfs v4.3+20151116
// Library Version: 101
//Execution Driver: native-0.2
//Logging Driver: json-file
//Kernel Version: 3.16.7-29-desktop
//Operating System: openSUSE 13.2 (Harlequin) (x86_64)
//CPUs: 8
//Total Memory: 15.6 GiB
//Name: workhorse.atsticks.ch
//ID: Q3SM:6EYV:MFLM:BFWF:S6T4:WDL3:MONH:4NF5:MKXC:H2R6:ZMQZ:DJ4B
//WARNING: No swap limit support
//
//     */
//    @Override
//    public void getDockerInfo(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker info");
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker:info:", result)));
//        }
//    }
//
//    /**
//     * Access a machine status.
//     * @param name the machine name , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineStatus(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine status " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker:info:", result)));
//        }
//    }
//
//    /**
//     * Access a machine ip.
//     * @param name the machine ip , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineIP(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine ip " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:ip:" + name, result)));
//        }
//    }
//
//    /**
//     * Access a machine status.
//     * @param name the machine name , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineUpgrade(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine upgrade " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:upgrade:" + name, result)));
//        }
//    }
//
//    /**
//     * Starts a machine.
//     * @param name the machine name , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineStart(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine start " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:start:" + name, result)));
//        }
//    }
//
//    /**
//     * Restarts a machine.
//     * @param name the machine name , not null.
//     * @return the result, not null
//     */
//    @Override
//    public void machineRestart(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine restart " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:restart:" + name, result)));
//        }
//    }
//
//    /**
//     * Stops a machine.
//     * @param name the machine name , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineStop(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine stop " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:stop:" + name, result)));
//        }
//    }
//
//    /**
//     * Kills a machine.
//     * @param name the machine name , not null.
//     * @return the status, not null
//     */
//    @Override
//    public void machineKill(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine kill " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:kill:" + name, result)));
//        }
//    }
//
//    /**
//     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
//     * @return a list with all io.github.jdocker.machine names, never null.
//     */
//    @Override
//    public void machineLS(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
////            if (!isDockerMachineInstalled()) {
////                handler.handle(Future.failedFuture("docker-machine not installed."));
////            }
//            String namesToParse = Executor.execute("docker-machine ls");
//            BufferedReader reader = new BufferedReader(new StringReader(namesToParse));
//            List<String> result = new ArrayList<>();
//            String line = null;
//            try {
//                line = reader.readLine();
//            } catch (IOException e) {
//                LOG.log(Level.SEVERE, "Failed to read String..???", e);
//            }
//            boolean inSection = false;
//            while (line != null) {
//                if (inSection && !line.contains("error") && !line.contains("ERROR:")) {
//                    // NAME   ACTIVE   DRIVER  STATE  URL  SWARM
//                    int index = line.indexOf(" ");
//                    if (index > 0) {
//                        result.add(line.substring(0, index));
//                    }
//                }
//                if (line.startsWith("NAME")) {
//                    inSection = true;
//                }
//                try {
//                    line = reader.readLine();
//                } catch (IOException e) {
//                    LOG.log(Level.SEVERE, "Failed to read String..???", e);
//                }
//            }
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:ls", result)));
//        }
//    }
//
//    /**
//     * Create a new machine with the given machine configuration.
//     * @param machineConfig the machine config, not null.
//     * @return the new machine, check its status if all is OK.
//     */
//    @Override
//    public void machineCreate(String agentName, MachineConfig machineConfig, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            if (machineConfig == null) {
//                throw new IllegalStateException("Cannot create machine without machine configuration.");
//            }
//            StringBuilder createCommand = new StringBuilder("docker-machine create ");
//            if (machineConfig.getDriver() != null) {
//                createCommand.append("--driver ").append(machineConfig.getDriver()).append(' ');
//            }
//            if (machineConfig.getInstallURL() != null) {
//                createCommand.append("--engine-install-url ").append(machineConfig.getInstallURL()).append(' ');
//            }
//            for (String opt : machineConfig.getEngineOptions()) {
//                createCommand.append("--engine-opt ").append(opt).append(' ');
//            }
//            for (String reg : machineConfig.getInsecureRegistries()) {
//                createCommand.append("--engine-insecure-registry ").append(reg).append(' ');
//            }
//            for (Map.Entry<String, String> en : machineConfig.getLabels().entrySet()) {
//                if (en.getKey().equals(en.getValue())) {
//                    createCommand.append("--engine-label ").append(en.getValue()).append(' ');
//                } else {
//                    createCommand.append("--engine-label ").append(en.getKey() + '=' + en.getValue()).append(' ');
//                }
//            }
//            if (machineConfig.getStorageDriver() != null) {
//                createCommand.append("--engine-storage-driver ").append(machineConfig.getStorageDriver()).append(' ');
//            }
//            for (String env : machineConfig.getMachineEnvironment()) {
//                createCommand.append("--engine-env ").append(env).append(' ');
//            }
//            // OPtional: swarm config
//            if (machineConfig.getSwarmConfig() != null) {
//                SwarmConfig swarmConfig = machineConfig.getSwarmConfig();
//                if (swarmConfig.getSwarmImage() != null) {
//                    createCommand.append("--swarm-image ").append(swarmConfig.getSwarmImage()).append(' ');
//                }
//                if (swarmConfig.isSwarmMaster()) {
//                    createCommand.append("--swarm-master ");
//                }
//                if (swarmConfig.getSwarmDiscoveryToken() != null) {
//                    createCommand.append("--swarm-discovery ").append(swarmConfig.getSwarmDiscoveryToken()).append(' ');
//                }
//                if (swarmConfig.getSwarmStrategy() != null) {
//                    createCommand.append("--swarm-strategy ").append(swarmConfig.getSwarmStrategy()).append(' ');
//                }
//                for (String env : swarmConfig.getSwarmEnvironment()) {
//                    createCommand.append("--swarm-opt ").append(env).append(' ');
//                }
//                if (swarmConfig.getSwarmHostURI() != null) {
//                    createCommand.append("--swarm-host ").append(swarmConfig.getSwarmHostURI()).append(' ');
//                }
//                if (swarmConfig.getSwarmAdvertizeURI() != null) {
//                    createCommand.append("--swarm-addr ").append(swarmConfig.getSwarmAdvertizeURI()).append(' ');
//                }
//            }
//            createCommand.append(' ').append(machineConfig.getName());
//            String command = createCommand.toString();
//            LOG.info("Creating new machine with: " + command);
//            String result = Executor.execute(command);
//            if (result.trim().isEmpty()) {
//                LOG.info("MACHINE " + machineConfig.getName() + " HAS BEEN CREATED.");
//            } else {
//                LOG.info("MACHINE CREATION PROBABLY FAILED: " + result);
//            }
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:create:"+machineConfig.getName(), result)));
//        }
//    }
//
//
//
//    /**
//     * Access a machine configuration by name.
//     * @param name the machine name , not null.
//     * @return the MachineConfig instance, or null.
//     */
//    @Override
//    public void machineInspect(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("docker-machine inspect " + name);
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "docker-machine:inspect:" + name, result)));
//        }
//    }
//
//
//
//    /*
//    docker0   Link encap:Ethernet  Hardware Adresse 02:42:40:5C:98:84
//          inet Adresse:172.17.0.1  Bcast:0.0.0.0  Maske:255.255.0.0
//          inet6 Adresse: fe80::42:40ff:fe5c:9884/64 Gültigkeitsbereich:Verbindung
//          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
//          RX packets:679 errors:0 dropped:0 overruns:0 frame:0
//          TX packets:821 errors:0 dropped:0 overruns:0 carrier:0
//          collisions:0 Sendewarteschlangenlänge:0
//          RX bytes:1967810 (1.8 Mb)  TX bytes:149518 (146.0 Kb)
//
//eno1      Link encap:Ethernet  Hardware Adresse A0:B3:CC:4E:46:AE
//          inet Adresse:192.168.1.4  Bcast:192.168.1.255  Maske:255.255.255.0
//          inet6 Adresse: fe80::a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Verbindung
//          inet6 Adresse: 2002:544a:d455:e472:a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Global
//          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
//          RX packets:939691 errors:0 dropped:0 overruns:0 frame:0
//          TX packets:571251 errors:0 dropped:9 overruns:0 carrier:0
//          collisions:0 Sendewarteschlangenlänge:1000
//          RX bytes:986884862 (941.1 Mb)  TX bytes:92269450 (87.9 Mb)
//
//lo        Link encap:Lokale Schleife
//          inet Adresse:127.0.0.1  Maske:255.0.0.0
//          inet6 Adresse: ::1/128 Gültigkeitsbereich:Maschine
//          UP LOOPBACK RUNNING  MTU:65536  Metric:1
//          RX packets:802403 errors:0 dropped:0 overruns:0 frame:0
//          TX packets:802403 errors:0 dropped:0 overruns:0 carrier:0
//          collisions:0 Sendewarteschlangenlänge:0
//          RX bytes:198349659 (189.1 Mb)  TX bytes:198349659 (189.1 Mb)
//
//vboxnet0  Link encap:Ethernet  Hardware Adresse 0A:00:27:00:00:00
//          UP BROADCAST MULTICAST  MTU:1500  Metric:1
//          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
//          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
//          collisions:0 Sendewarteschlangenlänge:1000
//          RX bytes:0 (0.0 b)  TX bytes:0 (0.0 b)
//     */
//    @Override
//    public void getIFConfig(String agentName, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            String result = Executor.execute("sudo ifconfig");
//            handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                    "system:ifconfig", result)));
//        }
//        // Look for
////        inet Adresse:192.168.1.4  Bcast:192.168.1.255  Maske:255.255.255.0
////        inet6 Adresse: fe80::a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Verbindung
////        inet6 Adresse: 2002:544a:d455:e472:a2b3:ccff:fe4e:46ae/64 Gültigkeitsbereich:Global
//    }
//
//
////    /**
////     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
////     * @return a list with all io.github.jdocker.machine names, never null.
////     */
////    @Override
////    public void getMachineNames(String agentName, Handler<AsyncResult<OwnedResult>> handler){
////        // Lookup in config clusters
////        List<String> machines = new ArrayList<>();
////        Configuration cfg = ConfigurationProvider.getConfiguration();
////        String namesVal = cfg.get("jdocker.machines");
////        if(namesVal!=null){
////            machines.addAll(Arrays.asList(namesVal.split(",")));
////        }
////        machines.add(this.machineConfig.getName());
////        handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
////                "system:machines", machines)));
////    }
//
//    /**
//     * Access a machine configuration by name.
//     * @param name the machine name , not null.
//     * @return the MachineConfig instance, or null.
//     */
//    @Override
//    public void getMachineConfig(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler){
//        if(matchHost(agentName)) {
//            Configuration cfg = ConfigurationProvider.getConfiguration();
//            String config = cfg.get("jdocker.machine." + name);
//            if (config != null) {
//                handler.handle(Future.succeededFuture(new OwnedResult("docker-agent:" + getAgentName(),
//                        "machines:" + name, Machine.from(config))));
//            }
//        }
//    }
//
//    private boolean matchHost(String agentName) {
//        return this.machineConfig.getName().matches(agentName);
//    }
//
//
//    private String[] loadScripts(String... urls)throws IOException{
//        List<String> result = new ArrayList<>();
//        for(String urlVal:urls){
//            URL url = new URL(urlVal);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//            StringBuilder b = new StringBuilder();
//            String line = reader.readLine();
//            while(line!=null){
//                b.append(line);
//            }
//            result.add(b.toString());
//        }
//        return result.toArray(new String[result.size()]);
//    }
//
//    private JsonObject createBaseJsonResponse(String action){
//        return new JsonObject().put("docker-agent", this.machineConfig.getName())
//                .put("action", action)
//                .put("type", "reply");
//    }
//
//}
