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

import java.util.*;
import java.util.logging.Logger;

/**
 * Simple Model of a docker-machine managed by docker docker-machine.
 */
public final class MachineConfig {
    private static final Logger LOG = Logger.getLogger(MachineConfig.class.getName());

    /** the pooled machine that should be used to install docker onto, should be accessible using ssh. */
    private Machine unmanagedMachine;

    /** The docker machine's name. */
    private String name;
    /** Driver to create io.github.jdocker.machine with. Maps to {@code --driver, -d "none"}. */
    private String driver;
    /** Custom DockerNodeRegistry install URL to use for engine installation [$MACHINE_DOCKER_INSTALL_URL], default is
     * {@code https://get.docker.com}. Maps to {@code --engine-install-url}.
     */
    private String installURL;
    /**
     * Specify arbitrary flags to include with the created engine in the form {@code flag=cardinality}. Maps to
     * {@code --engine-opt [--engine-opt option --engine-opt option] }.
     */
    private Set<String> engineOptions = new HashSet<>();
    /** Specify insecure registries to allow with the created engine. Maps to
     * {@code --engine-insecure-registry [--engine-insecure-registry option --engine-insecure-registry option]}.
     */
    private Set<String> insecureRegistries = new HashSet<>();
    /** Labels assigned to the io.github.jdocker.machine, maps to {@code --engine-label [--engine-label option --engine-label option] }. */
    private Map<String,String> labels = new HashMap<>();
    /** THe storage driver to be used, maps to {@code --engine-storage-driver }. */
    private String storageDriver;
    /** Define environment variables for the engine. Maps to
     * {@code --engine-env [--engine-env option --engine-env option]}. */
    private Set<String> machineEnvironment = new HashSet<>();

    private SwarmConfig swarmConfig;

    MachineConfig(MachineConfigBuilder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.driver = builder.getDriver();
        this.engineOptions.addAll(builder.getEngineOptions());
        this.insecureRegistries.addAll(builder.getInsecureRegistries());
        this.installURL = builder.getInstallURL();
        this.labels.putAll(builder.getLabels());
        this.storageDriver = builder.getStorageDriver();
        this.swarmConfig = builder.getSwarmConfig();
    }

    public String getName() {
        return name;
    }

    public Machine getInstallationTarget(){
        return unmanagedMachine;
    }

    public String getDriver() {
        if(this.unmanagedMachine!=null){
            return "generic";
        }
        return driver;
    }

    public String getInstallURL() {
        if(this.unmanagedMachine!=null){
            return this.unmanagedMachine.getUri().toString();
        }
        return installURL;
    }

    public SwarmConfig getSwarmConfig(){
        return swarmConfig;
    }

    public Set<String> getEngineOptions() {
        return Collections.unmodifiableSet(engineOptions);
    }

    public Set<String> getInsecureRegistries() {
        return Collections.unmodifiableSet(insecureRegistries);
    }

    public Map<String,String> getLabels() {
        return Collections.unmodifiableMap(labels);
    }

    public void addLabels(Map<String,String> labels) {
        this.labels.putAll(labels);
    }

    public String getStorageDriver() {
        return storageDriver;
    }

    public Set<String> getMachineEnvironment() {
        return Collections.unmodifiableSet(machineEnvironment);
    }

    public JsonObject toJSON(){
        JsonObject o = new JsonObject().
                put("type", MachineConfig.class.getName())
                .put("name", getName())
                .put("driver", getDriver())
                .put("uri", installURL);
        if(unmanagedMachine!=null){
            o.put("unmanagedMachine", unmanagedMachine.toJSON());
        }
        if(swarmConfig!=null) {
            o.put("swarmConfig", swarmConfig.toJson());
        }
        // labelsl
        JsonArray array = new JsonArray();
        for(Map.Entry<String,String> en:labels.entrySet()){
            if(en.getKey().equals(en.getValue())) {
                array.add(en.getValue());
            }
            else{
                array.add(en.getKey()+'='+en.getValue());
            }
        }
        o.put("labels", array);
        array = new JsonArray();
        for(String env:machineEnvironment){
            array.add(env);
        }
        o.put("machineEnvironment", array);
        array = new JsonArray();
        for(String env:insecureRegistries){
            array.add(env);
        }
        o.put("insecureRegistries", array);
        array = new JsonArray();
        for(String env:engineOptions){
            array.add(env);
        }
        o.put("engineOptions", array);
        return o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineConfig machine = (MachineConfig) o;
        return name.equals(machine.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "MachineConfig{" +
                "name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }

    /**
     * Creates a new builder for a machine. Building the machine will also call docker-machine to create the instance.
     * @param name the machine's name, not null.
     * @return the new builder.
     */
    public static MachineConfigBuilder builder(String name){
        return new MachineConfigBuilder(name);
    }

    /**
     * Creates a new builder for a machine. Building the machine will also call docker-machine to create the instance.
     * @param machine the unmanaged machine to be used as installation target.
     * @return the new builder.
     */
    public static MachineConfigBuilder builder(Machine machine){
        return new MachineConfigBuilder(machine);
    }
}
