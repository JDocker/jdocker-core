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

import io.github.jdocker.common.Executor;
import io.github.jdocker.network.AddressPool;
import io.github.jdocker.network.spi.IPAddressManagerSpi;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by atsticks on 18.01.16.
 */
public class CalicoIPAMSpi implements IPAddressManagerSpi {

    private Map<String,AddressPool> pools = new ConcurrentHashMap<>();
    private Set<String> networks = new TreeSet<>();

    @Override
    public void createNetwork(String name){
        this.networks.add(name);
        String result =  Executor.execute("docker network create -d calico --ipam-driver calico " + name);
        // TODO check for errors
    }

    /**
     * Remove the defined network.
     * @param name the network's name, not null.
     */
    @Override
    public void removeNetwork(String name){
        this.networks.remove(name);
        String result =  Executor.execute("docker network remove " + name);
        // TODO check for errors
    }

    /**
     * Get the currently defined network ids.
     * @return the network ids.
     */
    @Override
    public Set<String> getNetworks(){
        // TODO readable from calico or etcd?
        return Collections.unmodifiableSet(networks);
    }

    /**
     * This command adds all IP addresses between two IPs as Calico pool(s).<br/>
     * NOTE: Calico pools must be identified with a CIDR prefix, so in the case that the start and end of the range
     * are not on a single CIDR boundary, this command creates multiple pools such that the entire range is covered.<br/>
     * This command can be run on any Calico node.<br/>
     * Command syntax: {@code calicoctl pool range add <START_IP> <END_IP> [--ipip] [--nat-outgoing]}
     * @return the command output.
     */
    @Override
    public void addAddressPool(AddressPool pool){
        this.pools.put(pool.getName(), pool);
        String result = null;
        if(pool.getStartIP()!=null && pool.getEndIP()!=null) {
            String cmd = "calicoctl pool range add " + pool.getStartIP() + " " + pool.getEndIP();
            if (pool.isOutgoing()) cmd += " --nat-outgoing";
            if (pool.isIpip()) cmd += " --ipip";
            result = Executor.execute(cmd);
        }
        else{
            String cmd = "calicoctl pool add "+pool.getCidrExpression();
            if (pool.isOutgoing()) cmd += " --nat-outgoing";
            if (pool.isIpip()) cmd += " --ipip";
            result = Executor.execute(cmd);
        }
        // TODO check for errors in result
    }

    /**
     * his command is used to remove configured CIDR pools from Calico.

     The command can be run on any Calico node.

     Command syntax:

     calicoctl pool remove <CIDRS>...

     <CIDRS>: A single or list of CIDRs separated by spaces.
     * @param pool A single or list of CIDRs separated by spaces.
     * @return
     */
    @Override
    public void removeAddressPool(AddressPool pool){
        this.pools.remove(pool.getName());
        String result = null;
        if(pool.getCidrExpression()!=null){
            result = Executor.execute("calicoctl pool remove " + pool.getCidrExpression());
        }
        // TODO check for errors in result
    }

    @Override
    public AddressPool getAddressPool(String name){
        return this.pools.get(name);
        // TODO synch with Tamaya
    }

    /**
     * This command allows you to release an IP address that had been previously assigned to an endpoint. When an IP
     * address is released, it becomes available for assignment to any endpoint.<br/>
     * Note that this does not remove the IP from any existing endpoints that may be using it, so we generally
     * recommend you use this command to clean up addresses from endpoints that were not cleanly removed from Calico.<nr/>
     * This command can be run on any Calico node.
     * @param ip the ip address, not null
     * @return the command output
     */
    @Override
    public void releaseIP(String ip){
        String result = Executor.execute("calicoctl ipam release " + ip);
        // TODO check for errors
    }

    /**
     * This command prints information about a given IP address, such as special attributes defined for the IP or
     * whether the IP has been reserved by a user of the Calico IPAM.<br/>
     * This command can be run on any Calico node.<br/>
     * Command syntax: {@code calicoctl ipam info <IP>}.
     * @param ip the ip address, not null
     * @return the info text
     */
    @Override
    public String getIPInfo(String ip){
        return Executor.execute("calicoctl ipam info " + ip);
    }

    @Override
    public String getAddressPoolInfo(){
        String result = Executor.execute("calicoctl pool show");
        // TODO read into Collection type
        return result;
    }


}
