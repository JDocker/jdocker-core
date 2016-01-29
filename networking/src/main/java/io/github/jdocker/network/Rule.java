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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a Calico security profile rule.
 */
public class Rule {

    public enum Action{
        deny,
        allow
    }

    private Protocol protocol;
    private String sourceTag;
    private AddressPool sourceNet;
    private List<String> sourcePorts = new ArrayList<>();

    private String destTag;
    private AddressPool destNet;
    private List<String> destPorts = new ArrayList<>();

    private Action action;

    Rule(RuleBuilder builder){
        this.protocol = builder.protocol;
        this.sourceTag = Objects.requireNonNull(builder.sourceTag);
        this.sourceNet = Objects.requireNonNull(builder.sourceNet);
        this.sourcePorts.addAll(builder.sourcePorts);
        this.destTag = Objects.requireNonNull(builder.destTag);
        this.destNet = Objects.requireNonNull(builder.destNet);
        this.destPorts.addAll(builder.destPorts);
        this.action = Objects.requireNonNull(builder.action);
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getSourceTag() {
        return sourceTag;
    }

    public AddressPool getSourceNet() {
        return sourceNet;
    }

    public List<String> getSourcePorts() {
        return Collections.unmodifiableList(sourcePorts);
    }

    public String getDestTag() {
        return destTag;
    }

    public AddressPool getDestNet() {
        return destNet;
    }

    public List<String> getDestPorts() {
        return Collections.unmodifiableList(destPorts);
    }

    public Action getAction() {
        return action;
    }

    /**
     * Creates JSON representation:
     * <pre>
     * {
     "protocol": "tcp|udp|icmp|icmpv6",
     "src_tag": "<tag_name>",
     "src_net": "<CIDR>",
     "src_ports": [1234, "2048:4000"],
     "dst_tag": "<tag_name>",
     "dst_net": "<CIDR>",
     "dst_ports": [1234, "2048:4000"],
     "icmp_type": <int>,
     "action": "deny|allow",
     }
     * </pre>
     * @return
     */
    public String toJson(){
        StringBuilder b = new StringBuilder(256);
        b.append("{\n")
                .append("  \"protocol\": \"").append(protocol.toString()).append("\",\n")
                .append("  \"src_tag\": \"").append(sourceTag).append("\",\n")
                .append("  \"src_net\": \"").append(sourceNet.toString()).append("\",\n")
                .append("  \"src_ports\": \"").append(formatPorts(sourcePorts)).append("\",\n")
                .append("  \"dst_tag\": \"").append(sourceTag).append("\",\n")
                .append("  \"dst_net\": \"").append(sourceNet.toString()).append("\",\n")
                .append("  \"dst_ports\": [").append(formatPorts(sourcePorts)).append("],\n")
                .append("  \"action\": \"").append(action.toString()).append("\"\n")
                .append('}');
        return b.toString();
    }

    private String formatPorts(List<String> ports) {
        StringBuilder b = new StringBuilder();
        for(String port:ports){
            if(port.indexOf(':')>0){
                b.append("\"").append(port).append('\"');
            }
            else{
                b.append(port);
            }
            b.append(',');
        }
        if(b.length()>0){
            b.setLength(b.length()-1);
        }
        return b.toString();
    }

    public static RuleBuilder builder(){
        return new RuleBuilder();
    }

    public RuleBuilder toBuilder(){
        return new RuleBuilder(this);
    }

    @Override
    public String toString() {
        return "Rule{" +
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
