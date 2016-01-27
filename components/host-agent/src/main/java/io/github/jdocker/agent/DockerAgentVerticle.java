package io.github.jdocker.agent;

import io.github.jdocker.*;
import io.github.jdocker.common.Executor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;
import org.bouncycastle.util.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Docker process, which is able to perform deployment, monitoring as well as statistical functions.
 */
public class DockerAgentVerticle extends AbstractVerticle{

    private static final Logger LOG = Logger.getLogger(DockerAgentVerticle.class.getName());

    /** Default interval that defines how often an agent sends its heatbeat data. */
    private static final long DEFAULT_HEARTBEAT_INTERVAL_MS = 30000L; // all 30 seconds
    /** The current node's machine config. */
    private MachineConfig machineConfig;
    /** The id of zhe timer for sending the heartbeats. */
    private long timerID;

    public void start() {
        initLocalNode();
        // register node into known nodes
        EventBus eb = vertx.eventBus();
        // TODO access a clustered event bus here...

        eb.consumer(DockerAgentCommands.DOCKER_INSPECT.getName(),message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            message.reply(getMachineConfig(host).toJSON());
        });
        eb.consumer(DockerAgentCommands.AGENT_GET_LABELS.getName(), message -> {
            // TODO read from cluster config
        });
        eb.consumer(DockerAgentCommands.AGENT_SET_LABELS.getName(), message -> {
            // TODO persist into cluster config
        });
        eb.consumer(DockerAgentCommands.DOCKER_INFO.getName(), message -> {
            message.reply(getDockerInfo());
        });
        eb.consumer(DockerAgentCommands.DOCKER_INSPECT.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            message.reply(machineInspect(host));
        });
        eb.consumer(DockerAgentCommands.DOCKER_STATUS.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            message.reply(machineStatus(host));
        });
        eb.consumer(DockerAgentCommands.DOCKER_STOP.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineStop(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_START.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineStart(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_UPGRADE.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineUpgrade(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_RESTART.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineRestart(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_KILL.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineKill(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_IP.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            machineIP(host);
        });
        eb.consumer(DockerAgentCommands.DOCKER_DEPLOY.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
            vertx.executeBlocking(future -> {
                // Call some blocking API that takes a significant amount of time to return
                //            if(host.equals(this.getAgentName())){
//                // TODO
//                deployLocally(message.getBody());
//            }
//            else{
//                machineDeploy(host, message.getBody());
//            }
                future.complete(true);
            }, res -> {
                System.out.println("The result is: " + res.result());
            });

        });
        eb.consumer(DockerAgentCommands.DOCKER_CHECK_DEPLOYMENT.getName(), message -> {
            String host = message.headers().get("host");
            if(host==null){
                message.fail(1, "host header missing.");
            }
//            CHeck if this installation (container) matches these settings here...
            // and if the health state is OK for a deployment...
        });
        eb.consumer(DockerAgentCommands.DOCKER_SOFTWARE_CHECK.getName(), message -> {
            message.reply(new JsonObject()
                    .put("which", isWhichInstalled())
                    .put("calico", isCalicoInstalled())
                    .put("ssh", isSshInstalled())
                    .put("docker-machine", isCalicoInstalled())
                    .put("docker", isDockerInstalled()));
        });
        eb.send("jdocker.Docker.Agent:started", machineConfig.getName());
        Long heartbeatInternal = ConfigurationProvider.getConfiguration().get("jdocker.Docker.Agent.heartbeatInterval", Long.class);
        if(heartbeatInternal==null){
            heartbeatInternal = Long.valueOf(DEFAULT_HEARTBEAT_INTERVAL_MS);
        }
        timerID = vertx.setTimer(heartbeatInternal, id -> {
            // register node into known nodes
            EventBus bus = vertx.eventBus();
            bus.send("jdocker.Docker.Agent:heartbeat", createHeartbeat());
        });

    }

    private JsonObject createHeartbeat(){
        // TODO implement heartbeat returned
        return new JsonObject();
    }

    private void initLocalNode() {
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

    public void stop() {
        // remove nodes from known nodes.
        EventBus eb = vertx.eventBus();
        // tbd send removal event via vertx or adapt shared configuration...???
        eb.send("jdocker.Docker.Agent:stopped", machineConfig.getName());
    }

    public boolean isWhichInstalled(){
        String inst = Executor.execute("which sh");
        return inst.equals("/bin/sh") || inst.equals("/usr/bin/sh");
    }

    public boolean isDockerInstalled(){
        String inst = Executor.execute("which docker");
        return !inst.contains("no docker in");
    }

    public String installDocker()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Docker.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }

    public boolean isCalicoInstalled(){
        String inst = Executor.execute("which calico");
        return !inst.contains("no calico in");
    }

    public String installCalico()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Calico.installScripts");
        String[] scriptURLs = installScripts.split(",");
        String[] scripts = loadScripts(scriptURLs);
        return Executor.execute(scripts);
    }


    public boolean isSshInstalled(){
        String inst = Executor.execute("which ssh");
        return !inst.contains("no ssh in");
    }

    public boolean isDockerMachineInstalled(){
        String inst = Executor.execute("which docker-machine");
        return !inst.contains("no docker-achine in");
    }

    public String installDockerMachine()throws IOException{
        String installScripts = ConfigurationProvider.getConfiguration().get("jdocker.install.Docker-Machine.installScripts");
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
     * Create a new machine with the given machine configuration.
     * @param machineConfig the machine config, not null.
     * @return the new machine, check its status if all is OK.
     */
    public void machineCreate(MachineConfig machineConfig){
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
        for(String lbl:machineConfig.getLabels()){
            createCommand.append("--engine-label ").append(lbl).append(' ');
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
    }



    /**
     * Access a machine configuration by name.
     * @param name the machine name , not null.
     * @return the MachineConfig instance, or null.
     */
    public String machineInspect(String name){
        return Executor.execute("docker-machine inspect");
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
        Configuration cfg = ConfigurationProvider.getConfiguration();
        String namesVal = cfg.get("jdocker.machines");
        if(namesVal!=null){
            return Arrays.asList(namesVal.split(","));
        }
        return Collections.emptyList();
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
