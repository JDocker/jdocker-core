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
 * Interface representing an unpooled host.
 */
public class Machine {

    private String name;
    private URI uri;
    private Map<String,String> properties = new HashMap<>();

    public Machine(MachineBuilder builder){
        this.uri = builder.uri;
        this.name = builder.name;
        this.properties.putAll(builder.properties);
    }

    protected Machine(String name, URI uri, Map<String,String> properties){
        this.uri = Objects.requireNonNull(uri);
        this.name = Objects.requireNonNull(name);
        this.properties.putAll(properties);
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

    public JsonObject toJSON(){
        JsonObject ob = new JsonObject()
                .put("name", getName())
                .put("uri", getUri());
        JsonArray propsArray = new JsonArray();
        for(Map.Entry<String,String> en: properties.entrySet()){
            propsArray.add(new JsonObject().put(en.getKey(), en.getValue()));
        }
        ob.put("properties", propsArray);
        return ob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Machine)) return false;

        Machine machine = (Machine) o;

        return uri.equals(machine.uri);

    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public String toString() {
        return "Machine{" +
                "name='" + name + '\'' +
                ", uri=" + uri +
                ", properties=" + properties +
                '}';
    }

    public static MachineBuilder builder(URI uri){
        return new MachineBuilder(uri);
    }

    public static MachineBuilder builder(String name, URI uri){
        return new MachineBuilder(name, uri);
    }

    public static MachineConfig from(String json) {
        // TODO
        return null;
    }
}
