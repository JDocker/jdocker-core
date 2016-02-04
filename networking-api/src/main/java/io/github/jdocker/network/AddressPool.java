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

/**
 * Created by atsticks on 19.01.16.
 */
public class AddressPool {

    private String name;
    private String cidrExpression;
    private String startIP;
    private String endIP;
    private boolean ipip;
    private boolean outgoing;


    AddressPool(AddressPoolBuilder builder){
        this.name = builder.name;
        this.cidrExpression = builder.cidrExpression;
        this.endIP = builder.endIP;
        this.ipip = builder.ipip;
        this.outgoing = builder.outgoing;
        this.startIP = builder.startIP;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public String getName() {
        return name;
    }

    public String getCidrExpression() {
        return cidrExpression;
    }

    public String getStartIP() {
        return startIP;
    }

    public String getEndIP() {
        return endIP;
    }

    public boolean isIpip() {
        return ipip;
    }

    public static AddressPoolBuilder builder(String name){
        return new AddressPoolBuilder(name);
    }

    @Override
    public String toString() {
        return "AddressPool{" +
                "name='" + name + '\'' +
                ", cidrExpression='" + cidrExpression + '\'' +
                ", startIP='" + startIP + '\'' +
                ", endIP='" + endIP + '\'' +
                ", ipip=" + ipip +
                ", outgoing=" + outgoing +
                '}';
    }
}
