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
package io.github.jdocker.network;


import java.util.*;

/**
 * Security profile, e.g. as supported by calico.
 */
public class SecurityProfile {
    private String name;
    private List<Rule> inboundRules = new ArrayList<>();
    private List<Rule> outboundRules = new ArrayList<>();

    private Set<String> tags = new HashSet<>();

    SecurityProfile(SecurityProfileBuilder builder){
        this.name = builder.name;
        this.inboundRules.addAll(builder.inboundRules);
        this.outboundRules.addAll(builder.outboundRules);
        this.tags.addAll(builder.tags);
    }

    public String getName() {
        return name;
    }

    public List<Rule> getInboundRules() {
        return Collections.unmodifiableList(inboundRules);
    }

    public List<Rule> getOutboundRules() {
        return Collections.unmodifiableList(outboundRules);
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityProfile)) return false;

        SecurityProfile that = (SecurityProfile) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "SecurityProfile{" +
                "name='" + name + '\'' +
                ", inboundRules=" + inboundRules +
                ", outboundRules=" + outboundRules +
                ", tags=" + tags +
                '}';
    }

    /**
     * Create a new builder.
     * @param name the (unique) profile name, not null.
     * @return the new builder.
     */
    public static SecurityProfileBuilder builder(String name){
        return new SecurityProfileBuilder(name);
    }

    /**
     * Get a builder based on this profile to change its values.
     * @return a new builder, never null.
     */
    public SecurityProfileBuilder toBuilder(){
        return new SecurityProfileBuilder(this);
    }
}
