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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Interface representing an unpooled host.
 */
public class MachineBuilder {

    String name;
    URI uri;
    Map<String,String> properties = new HashMap<>();

    public MachineBuilder(URI uri){
        this(uri.toString(), uri);
    }

    public MachineBuilder(String name, URI uri){
        this.uri = Objects.requireNonNull(uri);
        this.name = name==null?uri.toString():name;
    }

    public MachineBuilder setProperties(Map<String,String> properties){
        this.properties.putAll(Objects.requireNonNull(properties));
        return this;
    }

    public MachineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MachineBuilder setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public MachineBuilder addProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    public MachineBuilder removeProperty(String key) {
        this.properties.remove(key);
        return this;
    }

    public Machine build(){
        return new Machine(this);
    }

}
