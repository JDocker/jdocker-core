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
package io.github.jdocker.networking;

import java.util.*;

/**
 * Created by atsticks on 18.01.16.
 */
public class SecurityProfileBuilder {

    List<Rule> inboundRules = new ArrayList<>();
    List<Rule> outboundRules = new ArrayList<>();
    String name;
    Set<String> tags = new HashSet<>();

    public SecurityProfileBuilder(String name){
        this.name = Objects.requireNonNull(name);
    }

    public SecurityProfileBuilder(SecurityProfile securityProfile) {
        this.name = securityProfile.getName();
        this.inboundRules.addAll(securityProfile.getInboundRules());
        this.outboundRules.addAll(securityProfile.getOutboundRules());
        this.tags.addAll(securityProfile.getTags());
    }

    public SecurityProfileBuilder addInboundRules(Rule... inboundRules) {
        for(Rule rule:inboundRules) {
            this.inboundRules.add(Objects.requireNonNull(rule));
        }
        return this;
    }

    public SecurityProfileBuilder removeInboundRule(int index){
        this.inboundRules.remove(index);
        return this;
    }

    public SecurityProfileBuilder removeInboundRule(Rule rule){
        this.inboundRules.remove(rule);
        return this;
    }

    public SecurityProfileBuilder removeOutboundRule(int index){
        this.outboundRules.remove(index);
        return this;
    }

    public SecurityProfileBuilder removeOutboundRule(Rule rule){
        this.outboundRules.remove(rule);
        return this;
    }

    public SecurityProfileBuilder addOutboundRules(Rule... outboundRules) {
        for(Rule rule:outboundRules) {
            this.outboundRules.add(Objects.requireNonNull(rule));
        }
        return this;
    }

    public SecurityProfileBuilder addTags(String... tags) {
        for(String tag:tags){
            this.tags.add(tag);
        }
        return this;
    }

    public SecurityProfileBuilder removeTags(String... tags) {
        for(String tag:tags){
            this.tags.remove(tag);
        }
        return this;
    }

    public SecurityProfileBuilder clearTags(){
        this.tags.clear();
        return this;
    }

    public SecurityProfileBuilder clearInboundRules(){
        this.inboundRules.clear();
        return this;
    }

    public SecurityProfileBuilder clearOutboundRules(){
        this.outboundRules.clear();
        return this;
    }

    public SecurityProfile build(){
        return new SecurityProfile(this);
    }

    @Override
    public String toString() {
        return "SecurityProfileBuilder{" +
                "inboundRules=" + inboundRules +
                ", outboundRules=" + outboundRules +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
