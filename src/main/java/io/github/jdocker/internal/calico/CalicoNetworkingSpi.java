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
package io.github.jdocker.internal.calico;

import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.common.Executor;
import io.github.jdocker.JDockerMachine;
import io.github.jdocker.networking.NetworkingStatus;
import io.github.jdocker.networking.SecurityProfile;
import io.github.jdocker.spi.NetworkingSpi;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by atsticks on 18.01.16.
 */
public class CalicoNetworkingSpi implements NetworkingSpi{

    private final Map<String,SecurityProfile> profiles = new ConcurrentHashMap<>();

    @Override
    public NetworkingStatus getStatus(){
        String result = Executor.execute("calicoctl status");
        if(result.contains("Error")){
            return NetworkingStatus.Error;
        }
        return NetworkingStatus.Running;
    }

    @Override
    public String getVersion(){
        return "Calico Networking: " + Executor.execute("calicoctl version");
    }

    @Override
    public void installNetworking(JDockerMachine machine){
        String result = Executor.executeRemote(machine, "calicoctl node --libnetwork");
        // TODO check for errors
    }

    @Override
    public void stopNetworking(JDockerMachine machine){
        String result =   Executor.executeRemote(machine, "sudo calicoctl node stop");
        // TODO check for errors
    }

    @Override
    public void startNetworking(JDockerMachine machine){
        String result =   Executor.executeRemote(machine, "sudo calicoctl node start");
        // TODO check for errors
    }


    /**
     * Adds the given container to the calico net, using eth1 as iface.
     * @param container the container to be added to the calico net, not null.
     * @param ip an ip address, a CIDR expression (192.168.27.0/24), or a IP type (ipv4, ipv6)
     * @return
     */
    public void addContainer(ContainerInfo container, String ip){
        addContainer(container, ip, null);
    }

    /**
     * Adds the given container to the calico net.
     * @param container the container to be added to the calico net, not null.
     * @param ip an ip address, a CIDR expression (192.168.27.0/24), or a IP type (ipv4, ipv6)
     * @param iface optional, default is eth1
     * @return
     */
    public static void addContainer(ContainerInfo container, String ip, String iface){
        String result = null;
        if(iface==null) {
            result = Executor.execute("calicoctl container add " + container.id() + " " + ip);
        }
        result = Executor.execute("calicoctl container add " + container.id() + " " + ip + " --interface=" + iface);
        // TODO check for errors
    }

    public static String addIPToContainer(ContainerInfo container, String ip){
        return addIPToContainer(container, ip, null);
    }

    public static String addIPToContainer(ContainerInfo container, String ip, String iface){
        if(iface==null) {
            Executor.execute("calicoctl container " + container.id() + " ip add " + ip + " --interface=" + iface);
        }
        return Executor.execute("calicoctl container " + container.id() + " ip add " + ip);
    }

    public static String removeIPFromContainer(ContainerInfo container, String ip){
        return addIPToContainer(container, ip, null);
    }

    public static String removeIPFromContainer(ContainerInfo container, String ip, String iface){
        if(iface==null) {
            Executor.execute("calicoctl container " + container.id() + " ip remove " + ip + " --interface=" + iface);
        }
        return Executor.execute("calicoctl container " + container.id() + " ip remove " + ip);
    }

    public static String removeContainer(ContainerInfo container){
        return Executor.execute("calicoctl container remove "+container.id());
    }

    @Override
    public void createSecurityProfile(SecurityProfile profile){
        this.profiles.put(profile.getName(), profile);
        String result = Executor.execute("calicoctl profile "+profile.getName() + " rule create " + toJson(profile));
    }

    private String toJson(SecurityProfile profile) {
        // TODO implement JSON representation for calico
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * TODO implement required JSON format...
     * <pre>
     *     {
     "id": "APP",
     "inbound_rules": [
     {
     "action": "allow",
     "protocol": "tcp",
     "dst_ports": [7890],
     "src_tag": "APP_7890"
     },
     {
     "action": "allow",
     "protocol": "icmp",
     "icmp_type": 8
     }
     ],
     "outbound_rules": [
     {
     "action": "allow"
     }
     ]
     }
     * </pre>
     * @param profile
     * @return
     */
    @Override
    public void updateSecurityProfile(SecurityProfile profile){
        this.profiles.put(profile.getName(), profile);
        String result = Executor.execute("calicoctl profile "+profile.getName() + " rule update " + toJson(profile));
    }

    @Override
    public SecurityProfile getSecurityProfile(String name){
        return this.profiles.get(name);
    }

    @Override
    public void removeSecurityProfile(SecurityProfile profile){
        this.profiles.remove(profile.getName());
        String result = Executor.execute("calicoctl profile "+profile.getName() + " remove");
        // TODO check result
    }

    @Override
    public void setSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        String result =  Executor.execute("calicoctl container "+container.id()+" profile set " + formatProfiles(profiles));
        // TODO check result
    }

    @Override
    public void addSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        String result =  Executor.execute("calicoctl container "+container.id()+" profile append " + formatProfiles(profiles));
        // TODO check result
    }

    @Override
    public void removeSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        String result = Executor.execute("calicoctl container "+container.id()+" profile remove " + formatProfiles(profiles));
        // TODO check result
    }

    @Override
    public SecurityProfile getSecurityProfile(ContainerInfo container) {
        return getSecurityProfile(container.id());
    }

    @Override
    public Collection<String> getSecurityProfiles(ContainerInfo container){
        String result = Executor.execute("calicoctl container "+container.id()+" profile show");
        // TODO parse result:
//        +------+
//        | Name |
//        +------+
//        | PROF |
//        | WEB  |
//        +------+
        return Collections.emptySet();
    }

    @Override
    public Collection<String> getSecurityProfiles(){
        return this.profiles.keySet();
//        String result = Executor.execute("calicoctl profile show");
//        // TODO parse result:
////        +------+
////        | Name |
////        +------+
////        | PROF |
////        | WEB  |
////        +------+
//        return Collections.emptySet();
    }

    public static Collection<String> getEndpointsForHost(String hostname){
        String data = Executor.execute("calicoctl endpoint show --host="+hostname+" --orchestrator=docker");
        // TODO parse. see Endpoint class
        return Collections.emptySet();
    }

    public static String getEndpoint(String endpointId){
        return Executor.execute("calicoctl endpoint show --endpoint="+endpointId+" --orchestrator=docker");
    }

    public static String getAllEndpoints(){
        return Executor.execute("calicoctl endpoint show");
    }

    public static String getEndpoints(ContainerInfo container){
        return Executor.execute("calicoctl container "+container.id()+" endpoint show");
    }

    public static String setSecurityProfiles(String endpoint, SecurityProfile... profiles){
        return Executor.execute("calicoctl endpoint "+endpoint+" profile set " + formatProfiles(profiles));
    }

    public static String addSecurityProfiles(String endpoint, SecurityProfile... profiles){
        return Executor.execute("calicoctl endpoint "+endpoint+" profile append " + formatProfiles(profiles));
    }

    public static String removeSecurityProfiles(String endpoint, SecurityProfile... profiles){
        return Executor.execute("calicoctl endpoint "+endpoint+" profile remove " + formatProfiles(profiles));
    }

    public static Collection<String> getSecurityProfiles(String endpoint){
        String result = Executor.execute("calicoctl endpoint "+endpoint+" profile show");
        // TODO parse result:
//        +------+
//        | Name |
//        +------+
//        | PROF |
//        | WEB  |
//        +------+
        return Collections.emptySet();
    }

    private static String formatProfiles(SecurityProfile[] profiles) {
        StringBuilder b = new StringBuilder();
        for(SecurityProfile p:profiles){
            b.append(p.getName()).append(' ');
        }
        return b.toString();
    }

}
