///*
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
//package io.github.jdocker.network;
//
//import io.github.jdocker.common.CommandDescriptor;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by atsticks on 24.01.16.
// */
//public final class NetworkAgentCommands {
//
//    private static Map<String, CommandDescriptor> COMMANDS = new HashMap<>();
//
//    public static final CommandDescriptor DEFINE_ADDRESSPOOL = register(CommandDescriptor.of("jdocker:net-define-pool", "<poolinfo>",
//            "SUCCESS | FAILED",
//            "Define a pool of IP addresses to be available for containers."));
//
//    public static final CommandDescriptor NETWROKING_STATUS = register(CommandDescriptor.of("jdocker:net-status", "<host>",
//            "Running | Error | Stopped",
//            "Access the current networking status."));
//
//    public static final CommandDescriptor NETWROKING_VERSION = register(CommandDescriptor.of("jdocker:net-version", "<host>",
//            "SUCCESS | FAILED",
//            "Define a pool of IP addresses to be available for containers."));
//
//    public static final CommandDescriptor NETWROKING_INSTALL = register(CommandDescriptor.of("jdocker:net-install", "<host>",
//            "SUCCESS | FAILED",
//            "Install networking software on the given instance.."));
//
//    public static final CommandDescriptor NETWROKING_INSTALL_CHECK = register(CommandDescriptor.of("jdocker:net-check-install", "<host>",
//            "true | false",
//            "Checks if networking software is installed on the given instance."));
//
//    public static final CommandDescriptor NETWROKING_START = register(CommandDescriptor.of("jdocker:net-start", "<host>",
//            "Running | Error | Stopped",
//            "Starts the networking layer on the given instance, returns the final networking status."));
//
//    public static final CommandDescriptor NETWROKING_STOP = register(CommandDescriptor.of("jdocker:net-stop", "<host>",
//            "Running | Error | Stopped",
//            "Stops the networking layer on the given instance, returns the final networking status."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_CREATE = register(CommandDescriptor.of("jdocker:net-profile-create", "<host> <profile>",
//            "SUCCESS | FAILED",
//            "Create a new security policy."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_UPDATE = register(CommandDescriptor.of("jdocker:net-profile-update", "<host> <profile>",
//            "SUCCESS | FAILED",
//            "Updates a security policy."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_REMOVE = register(CommandDescriptor.of("jdocker:net-profile-remove", "<host> <profileId>",
//            "SUCCESS | FAILED",
//            "Removes a security policy."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_GET = register(CommandDescriptor.of("jdocker:net-profile-get", "<host> <profileId>",
//            "<json>",
//            "Access a security policy."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_GET_ALL = register(CommandDescriptor.of("jdocker:net-profiles", "<host>",
//            "<jsonarray>",
//            "Access a security policy array of all policies known."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_CONTAINER_GET = register(CommandDescriptor.of("jdocker:net-profile-container-get", "<host> <containerId>",
//            "<jsonarray>",
//            "Access a security policies for a container (each container does have at least one)."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_CONTAINER_SET = register(CommandDescriptor.of("jdocker:net-profile-container-set", "<host> <containerId> <profile>",
//            "SUCCESS | FAIL",
//            "Sets the container policies for a container."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_CONTAINER_ADD = register(CommandDescriptor.of("jdocker:net-profile-container-add", "<host> <containerId> <profile>",
//            "SUCCESS | FAIL",
//            "Sets the container policies for a container."));
//
//    public static final CommandDescriptor NETWROKING_POLICY_CONTAINER_REMOVE = register(CommandDescriptor.of("jdocker:net-profile-container-remove", "<host> <containerId> <profileId>",
//            "SUCCESS | FAIL",
//            "Removes a security policy from a container."));
//
//    // TODO add IPAM commands
//
//
//    public static CommandDescriptor register(CommandDescriptor desc) {
//        if (COMMANDS.containsKey(desc.getName())) {
//            throw new IllegalArgumentException("Command already defined: " + desc.getName());
//        }
//        COMMANDS.put(desc.getName(), desc);
//        return desc;
//    }
//
//    public static Set<String> getCommandNames() {
//        return COMMANDS.keySet();
//    }
//
//    public static String getParameters(String command) {
//        CommandDescriptor desc = COMMANDS.get(command);
//        if(desc==null){
//            return desc.getParameters();
//        }
//        throw new IllegalArgumentException("No such command: " + command);
//    }
//
//    public static String getResult(String command) {
//        CommandDescriptor desc = COMMANDS.get(command);
//        if(desc==null){
//            return desc.getResult();
//        }
//        throw new IllegalArgumentException("No such command: " + command);
//    }
//
//    public static String getDescription(String command) {
//        CommandDescriptor desc = COMMANDS.get(command);
//        if(desc==null){
//            return desc.getDescription();
//        }
//        throw new IllegalArgumentException("No such command: " + command);
//    }
//
//    public static String getCommandSummary(){
//        StringBuilder b = new StringBuilder();
//        b.append("NetworkAgentCommands\n");
//        b.append("--------------------\n");
//        for(CommandDescriptor desc: COMMANDS.values()){
//            b.append(desc);
//        }
//        return b.toString();
//    }
//
//}
