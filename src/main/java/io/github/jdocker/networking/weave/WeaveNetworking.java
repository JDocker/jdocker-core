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

import io.github.jdocker.common.Executor;
import io.github.jdocker.machine.Machine;
import io.github.jdocker.machine.MachineConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * AddressPool connectivity layer, built on top of weave:
 * <pre>
 *     weave
 Usage:

 weave --help | help
 weave setup
 weave version
 weave launch        <same arguments as 'weave launch-router'>
 weave launch-router [--password <password>] [--nickname <nickname>]
 [--ipalloc-range <cidr> [--ipalloc-default-subnet <cidr>]]
 [--no-discovery] [--init-peer-count <count>]
 [--trusted-subnets <cidr>,...] <peer> ...
 weave launch-proxy  [-H <endpoint>] [--without-dns]
 [--no-default-ipalloc] [--no-rewrite-hosts]
 [--no-multicast-route]
 [--hostname-from-label <labelkey>]
 [--hostname-match <regexp>]
 [--hostname-replacement <replacement>]
 [--rewrite-inspect]
 weave launch-plugin [--no-multicast-route]
 weave env           [--restore]
 weave config
 weave dns-args
 weave connect       [--replace] [<peer> ...]
 weave forget        <peer> ...
 weave status        [targets | connections | peers | dns]
 weave report        [-f <format>]
 weave run           [--without-dns] [--no-rewrite-hosts] [--no-multicast-route]
 [<addr> ...] <docker run args> ...
 weave start         [<addr> ...] <container_id>
 weave attach        [<addr> ...] <container_id>
 weave detach        [<addr> ...] <container_id>
 weave dns-add       [<ip_address> ...] <container_id> [-h <fqdn>] |
 <ip_address> ... -h <fqdn>
 weave dns-remove    [<ip_address> ...] <container_id> [-h <fqdn>] |
 <ip_address> ... -h <fqdn>
 weave dns-lookup    <unqualified_name>
 weave hide          [<addr> ...]
 weave ps            [<container_id> ...]
 weave stop
 weave stop-router
 weave stop-proxy
 weave stop-plugin
 weave reset
 weave rmpeer        <peer_id>

 where <peer>     = <ip_address_or_fqdn>[:<port>]
 <cidr>     = <ip_address>/<routing_prefix_length>
 <addr>     = [ip:]<cidr> | net:<cidr> | net:default
 <endpoint> = [tcp://][<ip_address>]:<port> | [unix://]/path/to/socket
 <peer_id>  = <nickname> or weave internal peer ID

 * </pre>
 */
public class WeaveNetworking {

    private WeaveNetworking(){}

    public static String installWeave(boolean performSudo, String password){
        String pwdPart = password==null?"": "--password " + password;
        if(performSudo){
            return Executor.execute("sudo curl -L git.io/weave -o /usr/local/bin/weave", "sudo chmod +x /usr/local/bin/weave", "sudo /usr/local/bin/weave launch " + pwdPart);
        }
        return Executor.execute("curl -L git.io/weave -o /usr/local/bin/weave", "chmod +x /usr/local/bin/weave", "weave launch " + pwdPart);
    }

    public static String installWeaveOnMachine(MachineConfig machine){
        return Executor.execute("eval \"$(docker-machine env "+machine.getName()+")\"", "weave launch");
    }

    public static String getWeaveStatus(MachineConfig machine){
        return Executor.execute("eval \"$(docker-machine env "+machine.getName()+")\"", "eval \"$(weave env)\"");
    }

    public static String getWeaveVersion(){
        return Executor.execute("weave version");
    }

    public static String getContainers(){
        return Executor.execute("weave ps");
    }

    public static String getContainers(String containerID){
        return Executor.execute("weave ps " + Objects.requireNonNull(containerID));
    }

    public static String stopNetworking(){
        return Executor.execute("weave stop");
    }

    public static String stopRouter(){
        return Executor.execute("weave stop-router");
    }

    public static String stopRProxy(){
        return Executor.execute("weave stop-proxy");
    }

    public static String stopWeavePlugin(){
        return Executor.execute("weave stop-plugin");
    }

    public static String reset(){
        return Executor.execute("weave reset");
    }

    public static String removePeer(String peerID){
        return Executor.execute("weave rmpeer " + Objects.requireNonNull(peerID));
    }

    // weave status        [targets | connections | peers | dns]
    public static String getWeaveStatus(){
        return Executor.execute("weave status");
    }
    public static String getWeaveTargetStatus(){
        return Executor.execute("weave status targets");
    }
    public static String getWeaveConnectionStatus(){
        return Executor.execute("weave status connections");
    }
    public static String getWeavePeerStatus(){
        return Executor.execute("weave status peers");
    }
    public static String getWeaveDNSStatus(){
        return Executor.execute("weave status dns");
    }

    public static String getWeaveRepor(){
        return Executor.execute("weave report");
    }

    // --init-peer-count 3
    public static String clusterMachines(Machine... machines){
        return clusterMachines(Arrays.asList(machines));
    }

    public static String clusterMachines(Collection<Machine> machines) {
        Machine first = null;
        StringBuilder b = new StringBuilder();
        for(Machine m:machines) {
            if(first==null) {
                b.append(Executor.execute("eval \"$(docker-machine "+m.getName()+")\"",
                        "weave launch --init-peer-count " + machines.size()));
                first = m;
            }
            else{
                b.append(Executor.execute("eval \"$(docker-machine env \""+m.getName()+"\")\"",
                        "weave launch --init-peer-count " + machines.size(),
                        "weave connect "+first.getURL()));
            }
        }
        return b.toString();
    }


    public static String expose(Machine machine, String... networks){
        StringBuilder b = new StringBuilder("weave expose ");
        for(String net:networks){
            net = net.trim();
            if(!stopNetworking().startsWith("net:")){
                net = "net:"+net;
                b.append(net).append(' ');
            }
        }
        return Executor.execute("eval \"$(docker-machine "+machine.getName()+")\"", b.toString());
    }

}
