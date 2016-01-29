package io.github.jdocker.network.internal.calico;/*
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
//package io.github.jdocker.internal.calico;
//
//import java.util.*;
//
///**
// * Created by atsticks on 18.01.16.
// */
//public class CalicoEndpoint{
//
//    /** ID of the endpoint. */
//    private String id;
//    /** The endpoint's host name. */
//    private String hostname;
//    /** Orchestrator running the workloads. */
//    private String orchestratorID;
//    /** ID of the workload containing the endpoint. */
//    private String workloadID;
//    /** IP addresses assigned to the endpoint. */
//    private List<String> addresses = new ArrayList<>();
//    /** MAC address of the workload's Calico interface. */
//    private String macAddress;
//    /** Profiles associated with the endpoint. */
//    private Set<String> profiles = new HashSet<>();
//    /** State of the endpoint. */
//    private String state;
//
//    public String getId() {
//        return id;
//    }
//
//    public String getHostname() {
//        return hostname;
//    }
//
//    public String getOrchestratorID() {
//        return orchestratorID;
//    }
//
//    public String getWorkloadID() {
//        return workloadID;
//    }
//
//    public List<String> getAddresses() {
//        return Collections.unmodifiableList(addresses);
//    }
//
//    public String getMacAddress() {
//        return macAddress;
//    }
//
//    public Set<String> getProfiles() {
//        return Collections.unmodifiableSet(profiles);
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CalicoEndpoint)) return false;
//
//        CalicoEndpoint endpoint = (CalicoEndpoint) o;
//
//        return id.equals(endpoint.id);
//
//    }
//
//    @Override
//    public int hashCode() {
//        return id.hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return "Endpoint{" +
//                "id='" + id + '\'' +
//                ", hostname='" + hostname + '\'' +
//                ", orchestratorID='" + orchestratorID + '\'' +
//                ", workloadID='" + workloadID + '\'' +
//                ", addresses=" + addresses +
//                ", macAddress='" + macAddress + '\'' +
//                ", profiles=" + profiles +
//                ", state=" + state +
//                '}';
//    }
//}
