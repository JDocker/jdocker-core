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

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a docker host, implemented by each running DocherHost Verticle.
 */
public class DockerHost {

    private String name;
    private URI uri;
    private Map<String,String> properties = new HashMap<>();

    private int cpus;
    private int memory;
    private long disksize;
    private MachineStatus status = MachineStatus.Unknown;

    DockerHost(DockerHostBuilder builder){
        this.uri = Objects.requireNonNull(builder.uri);
        this.name = Objects.requireNonNull(builder.name);
        this.properties.putAll(builder.properties);
        this.cpus = builder.cpus;
        this.memory = builder.memory;
        this.disksize = builder.disksize;
        this.status = builder.status;
    }

    /**
     * Get the global machine name, by default this equals to the URI, but the instances can be renamed to something
     * more meaningful. In most cases (depending on the renaming logic), the JDocker DNS infrastrcuture should be
     * alble to resolve this names similarly.
     *
     * @return the global machine name, not null.
     */
    public String getName(){
        return name;
    }

    /**
     * Get the docker-machine^s URI.
     * @return the URI, something like {@code tcp:192.16.834.23}.
     */
    public URI getUri(){
        return uri;
    }

    /**
     * Properties, e.g. for accessing the machine with ssh or similar.
     * @return the hsot properties, not null.
     */
    public Map<String,String> getProperties(){
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Get the number of CPUs of the machine.
     * @return the number of CPUs.
     */
    public int getCPUs(){
        return cpus;
    }

    /**
     * Get the memory available.
     * @return the memory on the machine.
     */
    public int getMemory(){
        return memory;
    }

    /**
     * Get the container's disk size.
     * @return the container's disk size.
     */
    public long getDiskSize(){
        return disksize;
    }

    /**
     * Get the current machine status.
     * @return the machine's status.
     */
    public MachineStatus getStatus(){
        return status;
    }

    /**
     * Get the machine's simple name, similar to a class name without package name part.
     * @return the simple name, not null.
     */
    public String getSimpleName(){
        int index = getName().lastIndexOf('.');
        if(index<0){
            return getName();
        }
        return getName().substring(index+1);
    }

    /**
     * Get the package name of the machine.
     * @return the package name, or an empty String, but never null.
     */
    public String getPacakge(){
        int index = getName().lastIndexOf('.');
        if(index<0){
            return "";
        }
        return getName().substring(0,index);
    }

    public JsonObject toJSON(){
        JsonObject ob = new JsonObject()
                .put("name", getName())
                .put("uri", getUri());
        JsonArray propsArray = new JsonArray();
        for(Map.Entry<String,String> en: properties.entrySet()){
            propsArray.add(new JsonObject().put(en.getKey(), en.getValue()));
        }
        ob.put("properties", propsArray);
        ob.put("cpus", getCPUs())
                .put("memory", getMemory())
                .put("disksize", getDiskSize())
                .put("status", getStatus().toString());
        return ob;
    }

    public static DockerHostBuilder builder(String name, URI uri){
        return new DockerHostBuilder(name, uri);
    }

    public static DockerHostBuilder builder(URI uri){
        return new DockerHostBuilder(uri);
    }

    public DockerHostBuilder builder(){
        return new DockerHostBuilder(getName(), getUri()).setProperties(getProperties())
                .setCpus(cpus).setMemory(memory).setDisksize(disksize).setStatus(status);
    }


}
