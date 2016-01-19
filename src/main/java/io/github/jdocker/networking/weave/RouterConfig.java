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
package io.github.jdocker.networking.weave;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 *     "Router": {
 "Protocol": "weave",
 "ProtocolMinVersion": 1,
 "ProtocolMaxVersion": 2,
 "Encryption": false,
 "PeerDiscovery": true,
 "Name": "ea:15:f3:ba:08:a6",
 "NickName": "workhorse.atsticks.ch",
 "Port": 6783,
 "Peers": [
 {
 Peer...
 }
 ],
 "UnicastRoutes": [
 {
 "Dest": "ea:15:f3:ba:08:a6",
 "Via": "00:00:00:00:00:00"
 }
 ],
 "BroadcastRoutes": [
 {
 "Source": "ea:15:f3:ba:08:a6",
 "Via": null
 }
 ],
 "Connections": null,
 "Targets": null,
 "OverlayDiagnostics": {
 "fastdp": {
 "Vports": [
 {
 "ID": 0,
 "Name": "datapath",
 "TypeName": "internal"
 },
 {
 "ID": 1,
 "Name": "vethwe-datapath",
 "TypeName": "netdev"
 },
 {
 "ID": 2,
 "Name": "vxlan-6784",
 "TypeName": "vxlan"
 }
 ],
 "Flows": []
 },
 "sleeve": null
 },
 "TrustedSubnets": [],
 "Interface": "datapath (via ODP)",
 "CaptureStats": {
 "FlowMisses": 0
 },
 "MACs": null
 },
 * </pre>
 */
public class RouterConfig {
    private String protocol = "weave";
    private int protocolMinVersion = 1;
    private int protocolMaxVersion = 2;
    private boolean encrypted = false;
    private boolean peerDiscoveryActive = true;
    private String name; // eg "ea:15:f3:ba:08:a6",
    private String nickName; // eg. workhorse.atsticks.ch",
    private int port = 6783; // weave default

    private Set<Peer> peers = new HashSet<>();
    private Set<Route> unicastRoutes = new HashSet<>();
    private Set<Route> broadcastRoutes = new HashSet<>();

    public String getProtocol() {
        return protocol;
    }

    public int getProtocolMinVersion() {
        return protocolMinVersion;
    }

    public int getProtocolMaxVersion() {
        return protocolMaxVersion;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean isPeerDiscoveryActive() {
        return peerDiscoveryActive;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public int getPort() {
        return port;
    }

    public Set<Peer> getPeers() {
        return peers;
    }

    public Set<Route> getUnicastRoutes() {
        return unicastRoutes;
    }

    public Set<Route> getBroadcastRoutes() {
        return broadcastRoutes;
    }
}
