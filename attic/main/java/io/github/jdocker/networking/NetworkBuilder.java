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

import java.util.Objects;

/**
 * Created by atsticks on 19.01.16.
 */
public class NetworkBuilder {

    String name;
    String cidrExpression;
    String startIP;
    String endIP;
    boolean ipip;
    boolean outgoing;

    public NetworkBuilder(String name){
        this.name = Objects.requireNonNull(name);
    }

    public NetworkBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NetworkBuilder setCidrExpression(String cidrExpression) {
        this.cidrExpression = cidrExpression;
        return this;
    }

    public NetworkBuilder setStartIP(String startIP) {
        this.startIP = startIP;
        return this;
    }

    public NetworkBuilder setEndIP(String endIP) {
        this.endIP = endIP;
        return this;
    }

    public NetworkBuilder setIpip(boolean ipip) {
        this.ipip = ipip;
        return this;
    }

    public NetworkBuilder setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
        return this;
    }

    public AddressPool build(){
        return new AddressPool(this);
    }

    @Override
    public String toString() {
        return "AddressPoolBuilder{" +
                "name='" + name + '\'' +
                ", cidrExpression='" + cidrExpression + '\'' +
                ", startIP='" + startIP + '\'' +
                ", endIP='" + endIP + '\'' +
                ", ipip=" + ipip +
                ", outgoing=" + outgoing +
                '}';
    }
}
