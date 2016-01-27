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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atsticks on 18.01.16.
 */
public class RuleBuilder {

    Protocol protocol = Protocol.tcp;
    String sourceTag;
    AddressPool sourceNet;
    List<String> sourcePorts = new ArrayList<>();

    String destTag;
    AddressPool destNet;
    List<String> destPorts = new ArrayList<>();

    Rule.Action action = Rule.Action.allow;

    public RuleBuilder(){
    }

    public RuleBuilder(Rule rule) {
        this.protocol = rule.getProtocol();
        this.sourceNet = rule.getSourceNet();
        this.sourcePorts.addAll(rule.getSourcePorts());
        this.sourceTag = rule.getSourceTag();
        this.destNet = rule.getDestNet();
        this.destPorts.addAll(rule.getDestPorts());
        this.destTag = rule.getDestTag();
        this.action = rule.getAction();
    }

    public RuleBuilder setAction(Rule.Action action) {
        this.action = action;
        return this;
    }

    public RuleBuilder setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public RuleBuilder setSourceTag(String sourceTag) {
        this.sourceTag = sourceTag;
        return this;
    }

    public RuleBuilder setSourceNet(AddressPool sourceNet) {
        this.sourceNet = sourceNet;
        return this;
    }

    public RuleBuilder setSourcePorts(List<String> sourcePorts) {
        this.sourcePorts = sourcePorts;
        return this;
    }

    public RuleBuilder setDestTag(String destTag) {
        this.destTag = destTag;
        return this;
    }

    public RuleBuilder setDestNet(AddressPool destNet) {
        this.destNet = destNet;
        return this;
    }

    public RuleBuilder setDestPorts(List<String> destPorts) {
        this.destPorts = destPorts;
        return this;
    }

    public Rule build(){
        return new Rule(this);
    }

    @Override
    public String toString() {
        return "RuleBuilder{" +
                "protocol=" + protocol +
                ", sourceTag='" + sourceTag + '\'' +
                ", sourceNet=" + sourceNet +
                ", sourcePorts=" + sourcePorts +
                ", destTag='" + destTag + '\'' +
                ", destNet=" + destNet +
                ", destPorts=" + destPorts +
                ", action=" + action +
                '}';
    }
}
