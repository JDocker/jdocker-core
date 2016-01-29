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
package io.github.jdocker.health;

import io.github.jdocker.common.CommandDescriptor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by atsticks on 24.01.16.
 */
public final class HealthAgentCommands {

    private static Map<String, CommandDescriptor> COMMANDS = new HashMap<>();

    public static final CommandDescriptor AGENT_HEALTHMANAGER_INFO = register(CommandDescriptor.of("jdocker:healthManager:info", "[name]",
                    new JsonObject().put("healthManager",
                            new JsonArray()
                                    .add(new JsonObject().put("name", "healthManager1"))
                                    .add(new JsonObject().put("description", "HealthManager running on agent XY."))
                                    .add(new JsonObject().put("healthChecks", new JsonArray()
                                    .add(new JsonObject().put("total", 20))
                                            .add(new JsonObject().put("success", 18))
                                            .add(new JsonObject().put("warn", 1))
                                            .add(new JsonObject().put("fail", 1))))).toString(),
            "Get the basic health manager information, optionally the manager's name can be passed, so only the " +
                    "HealthManager with the given name should repond."));

    public static final CommandDescriptor AGENT_HEALTHMANAGER = register(CommandDescriptor.of("jdocker:healthManager:info", "[name]",
            new JsonObject().put("healthManager",
                    new JsonArray()
                            .add(new JsonObject().put("name", "healthManager1"))
                            .add(new JsonObject().put("description", "HealthManager running on agent XY."))
                            .add(new JsonObject().put("healthChecks", new JsonArray()
                                    .add(new JsonObject().put("total", 20))
                                    .add(new JsonObject().put("success", 18))
                                    .add(new JsonObject().put("warn", 1))
                                    .add(new JsonObject().put("fail", 1))))).toString(),
            "Get the concrete ."));


    public static CommandDescriptor register(CommandDescriptor desc) {
        if (COMMANDS.containsKey(desc.getName())) {
            throw new IllegalArgumentException("Command already defined: " + desc.getName());
        }
        COMMANDS.put(desc.getName(), desc);
        return desc;
    }

    public static Set<String> getCommandNames() {
        return COMMANDS.keySet();
    }

    public static String getParameters(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.getParameters();
        }
        throw new IllegalArgumentException("No such command: " + command);
    }

    public static String getResult(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.getResult();
        }
        throw new IllegalArgumentException("No such command: " + command);
    }

    public static String getDescription(String command) {
        CommandDescriptor desc = COMMANDS.get(command);
        if(desc==null){
            return desc.getDescription();
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
