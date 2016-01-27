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
package io.github.jdocker;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by atsticks on 24.01.16.
 */
public final class DockerAgentCommands {

    private static Map<String, CommandDescriptor> COMMANDS = new HashMap<>();

    public static final CommandDescriptor AGENT_GET_LABELS = register(CommandDescriptor.of("jdocker:docker-read-labels", "<host>",
                    new JsonObject().put("agent-labels",
                            new JsonArray()
                                    .add(new JsonObject().put("key", "someValue"))
                                    .add(new JsonObject().put("key2", "someValue2"))).toString(),
            "Get the labels/properties of the agent."));
    public static final CommandDescriptor AGENT_SET_LABELS = register(CommandDescriptor.of("jdocker:docker-write-labels",
            "<host> -> " + new JsonObject().put("agent-labels",
                new JsonArray()
                        .add(new JsonObject().put("key", "someValue"))
                        .add(new JsonObject().put("key2", "someValue2"))).toString(),
            "Set the labels/properties of the agent."));
    public static final CommandDescriptor DOCKER_VERSION = register(CommandDescriptor.of("jdocker:docker-version",
            "<host>", new JsonObject().put("client", new JsonObject()
                    .put("version", "1.9.1").put("API version", "1.21")
                    .put("Go version", "go1.4.2").put("Git commit", "a32a1d5")
                    .put("Built","").put("OS/Arch","linux/amd64"))
                    .put("server", new JsonObject()
                            .put("version", "1.9.1").put("API version", "1.21")
                            .put("Go version", "go1.4.2").put("Git commit", "a32a1d5")
                            .put("Built","").put("OS/Arch","linux/amd64")).toString(),
            "Get the version information of the local docker instance."));
    public static final CommandDescriptor DOCKER_INFO = register(CommandDescriptor.of("jdocker:docker-info",
            "<host>", "Value returned by 'docker info'.", "Get the general information about the local docker instane. Example:\ncontainers: 34\n" +
                    "Images: 222\n" +
                    "Server Version: 1.9.1\n" +
                    "Storage Driver: btrfs\n" +
                    " Build Version: Btrfs v4.3+20151116\n" +
                    " Library Version: 101\n" +
                    "Execution Driver: native-0.2\n" +
                    "Logging Driver: json-file\n" +
                    "Kernel Version: 3.16.7-29-desktop\n" +
                    "Operating System: openSUSE 13.2 (Harlequin) (x86_64)\n" +
                    "CPUs: 8\n" +
                    "Total Memory: 15.6 GiB\n" +
                    "Name: workhorse.atsticks.ch\n" +
                    "ID: Q3SM:6EYV:MFLM:BFWF:S6T4:WDL3:MONH:4NF5:MKXC:H2R6:ZMQZ:DJ4B\n" +
                    "WARNING: No swap limit support"));
    public static final CommandDescriptor DOCKER_START = register(CommandDescriptor.of("jdocker:docker-start",
            "<dockerHost>",
            new JsonObject().put("action", "docker-start").put("dockerHost", "mainDocker.intra.net").put("output", "SUCCESS").put("success", true).toString(),
            "Starts a non running docker container."));
    public static final CommandDescriptor DOCKER_STOP = register(CommandDescriptor.of("jdocker:docker-start",
            "<dockerHost>",
            new JsonObject().put("action", "docker-stop").put("dockerHost", "mainDocker.intra.net").put("output", "SUCCESS").put("success", true).toString(),
            "Stops a running docker container."));
    public static final CommandDescriptor DOCKER_RESTART = register(CommandDescriptor.of("jdocker:docker-restart",
            "<dockerHost>",
            new JsonObject().put("action", "docker-restart").put("dockerHost", "mainDocker.intra.net").put("output", "SUCCESS").put("success", true).toString(),
            "Restarts a non docker container."));
    public static final CommandDescriptor DOCKER_KILL = register(CommandDescriptor.of("jdocker:docker-kill",
            "<dockerHost>",
            new JsonObject().put("action", "docker-kill").put("dockerHost", "mainDocker.intra.net").put("output", "FAILED").put("success", false).toString(),
            "Stops and removes a docker container."));
    public static final CommandDescriptor DOCKER_STATUS = register(CommandDescriptor.of("jdocker:docker-status",
            "<dockerHost>",
            new JsonObject().put("action", "docker-status").put("dockerHost", "mainDocker.intra.net").put("output", "Running").put("success", true).toString(),
            "Stops and removes a docker container."));
    public static final CommandDescriptor DOCKER_INSPECT = register(CommandDescriptor.of("jdocker:docker-inspect",
            "<dockerHost> <containerId>",
            new JsonObject().put("action", "docker-inspect").put("dockerHost", "mainDocker.intra.net").put("container", "12212g12u1u1z21212iziu21212o1u29182").put("data", "<inspection-json>").put("success", true).toString(),
            "Read out the current complete status of a running container."));
    public static final CommandDescriptor DOCKER_IP = register(CommandDescriptor.of("jdocker:docker-ip",
            "<dockerHost>",
            "<ipAddress>",
            "Get the IP address correspoinding a docherHost machine."));
    public static final CommandDescriptor DOCKER_UPGRADE = register(CommandDescriptor.of("jdocker:docker-upgrade",
            "<dockerHost>",
            new JsonObject().put("action", "docker-upgrade").put("dockerHost", "mainDocker.intra.net").put("output", "FAILED").put("success", true).toString(),
            "Upgrade the docker version installed on the machine."));
    public static final CommandDescriptor DOCKER_CHECK_DEPLOYMENT = register(CommandDescriptor.of("jdocker.Docker:checkDeployment",
            new JsonObject().put("TODO", true).toString(),
            new JsonObject().put("action", "docker-checkDeployment").put("dockerHost", "mainDocker.intra.net").put("details", "<deployment-check-data>").put("success", true).toString()
            , "Checks if the given deployment could be targeted by this node, returns the docker host name."));
    public static final CommandDescriptor DOCKER_DEPLOY = register(CommandDescriptor.of("jdocker.Docker:deploy",
            "<dockerHost> -> <deployment-json>",
            new JsonObject().put("action", "docker-deploy").put("dockerHost", "mainDocker.intra.net").put("details", "<deployment-data>").put("success", true).toString(),
                 "Checks if the given deployment could be targeted by this node, returns the docker host name."));

    public static final CommandDescriptor DOCKER_SOFTWARE_CHECK = register(CommandDescriptor.of("jdocker.Docker:checkSW",
            "<dockerHost>", "<software-check-json>", "Checks the software installed on the agent's machine."));
    public static final CommandDescriptor DOCKER_SOFTWARE_INSTALL = register(CommandDescriptor.of("jdocker.Docker:installSW",
            "<dockerHost>", "<software-install-json>", "Installs the software required on the agent's machine, the agent requires an internet connection."));

    public static CommandDescriptor register(CommandDescriptor desc) {
        if (COMMANDS.containsKey(desc.commandName)) {
            throw new IllegalArgumentException("Command already defined: " + desc.commandName);
        }
        COMMANDS.put(desc.commandName, desc);
        return desc;
    }

    public static Set<String> getCommandNames() {
        return COMMANDS.keySet();
    }

    public static String getParameters(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.parameters;
        }
        throw new IllegalArgumentException("No such command: " + command);
    }

    public static String getResult(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.result;
        }
        throw new IllegalArgumentException("No such command: " + command);
    }

    public static String getDescription(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.description;
        }
        throw new IllegalArgumentException("No such command: " + command);
    }

    public static String getCommandSummary(){
        StringBuilder b = new StringBuilder();
        b.append("DockerAgentCommands\n");
        b.append("-------------------\n");
        for(CommandDescriptor desc: COMMANDS.values()){
            b.append(desc);
        }
        return b.toString();
    }

}
