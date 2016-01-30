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
package io.github.jdocker.networking.internal.calico;

import io.github.jdocker.common.Endpoint;

import java.net.URI;
import java.util.ArrayList;

import java.util.*;

/**
 * Extended endpoint desciptor for a container endpoint managed with Calico.
 */
public class CalicoEndpoint extends Endpoint{

    /** Orchestrator running the workloads. */
    private String orchestratorID;
    /** ID of the workload containing the endpoint. */
    private String workloadID;
    /** IP addresses assigned to the endpoint. */
    private List<String> addresses = new ArrayList<>();
    /** MAC address of the workload's Calico interface. */
    private String macAddress;
    /** Profiles associated with the endpoint. */
    private Set<String> profiles = new HashSet<>();
    /** State of the endpoint. */
    private String state;

    public CalicoEndpoint(String name){
        super(name);
    }

    public CalicoEndpoint(String name, URI uri) {
        super(name, uri);
    }

    public String getOrchestratorID() {
        return orchestratorID;
    }

    public String getWorkloadID() {
        return workloadID;
    }

    public List<String> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public Set<String> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public String getState() {
        return state;
    }

    public CalicoEndpoint setOrchestratorID(String orchestratorID) {
        ensureClosed();
        this.orchestratorID = orchestratorID;
        return this;
    }

    public CalicoEndpoint setWorkloadID(String workloadID) {
        ensureClosed();
        this.workloadID = workloadID;
        return this;
    }

    public CalicoEndpoint setAddresses(List<String> addresses) {
        ensureClosed();
        this.addresses.clear();
        this.addresses.addAll(addresses);
        return this;
    }

    public CalicoEndpoint addAddresses(String... addresses) {
        ensureClosed();
        for(String adr:addresses) {
            this.addresses.add(adr);
        }
        return this;
    }

    public CalicoEndpoint setMacAddress(String macAddress) {
        ensureClosed();
        this.macAddress = macAddress;
        return this;
    }

    public CalicoEndpoint addProfiles(String... profiles) {
        ensureClosed();
        for(String prof:profiles) {
            this.profiles.add(prof);
        }
        return this;
    }

    public CalicoEndpoint setState(String state) {
        ensureClosed();
        this.state = state;
        return this;
    }

    public CalicoEndpoint close(){
        return (CalicoEndpoint)super.close();
    }

    @Override
    public String toString() {
        return "CalicoEndpoint{" +
                "name='" + getName() + '\'' +
                ", host='" + getHost() + '\'' +
                ", orchestratorID='" + orchestratorID + '\'' +
                ", workloadID='" + workloadID + '\'' +
                ", addresses=" + addresses +
                ", macAddress='" + macAddress + '\'' +
                ", profiles=" + profiles +
                ", state=" + state +
                '}';
    }
}
