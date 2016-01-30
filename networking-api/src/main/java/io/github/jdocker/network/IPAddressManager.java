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

import io.github.jdocker.common.ServiceContextManager;
import io.github.jdocker.network.spi.IPAddressManagerSpi;

import java.util.Collection;
import java.util.Set;

/**
 * Singleton for ID adress management.
 */
public final class IPAddressManager {

    private static final IPAddressManagerSpi ipamSpi = ServiceContextManager.getServiceContext().getService(IPAddressManagerSpi.class);

    private IPAddressManager(){}


    /**
     * Create a network to be used by containers with complete visibility.
     * @param name the network name, not null.
     */
    public static void createNetwork(String name, AddressPool... pools){
        ipamSpi.createNetwork(name, pools);
    }

    /**
     * Remove the defined network.
     * @param name the network's name, not null.
     */
    public static void removeNetwork(String name){
        ipamSpi.removeNetwork(name);
    }

    /**
     * Get the currently defined network ids.
     * @return the network ids.
     */
    public static Set<String> getNetworks(){
        return ipamSpi.getNetworks();
    }

    /**
     * This command adds the given address pool to the addresses assignable.<br/>
     * @param pool the ip address pool.
     * @return the command output.
     */
    public static void addAddressPool(AddressPool pool, String network){
        ipamSpi.addAddressPool(pool, network);
    }

    /**
     * This command is used to remove configured CIDR pools.
     * @param pool The pool to be removed.
     */
    public static void removeAddressPool(AddressPool pool, String network){
        ipamSpi.removeAddressPool(pool, network);
    }

    /**
     * This command allows you to release an IP address that had been previously assigned to an endpoint. When an IP
     * address is released, it becomes available for assignment to any endpoint.<br/>
     * Note that this does not remove the IP from any existing endpoints that may be using it, so we generally
     * recommend you use this command to clean up addresses from endpoints that were not cleanly removed.<nr/>
     * @param ip the ip address, not null
     * @return the command output
     */
    public static void releaseIP(String ip){
        ipamSpi.releaseIP(ip);
    }

    /**
     * This command prints information about a given IP address, such as special attributes defined for the IP or
     * whether the IP has been reserved by a user.<br/>
     * @param ip the ip address, not null
     * @return the info text
     */
    public static String getIPInfo(String ip){
        return ipamSpi.getIPInfo(ip);
    }

    /**
     * Access general info about the known address pools.
     * @return a general info on the current pools, not null.
     */
    public static String getAddressPoolInfo(){
        return ipamSpi.getAddressPoolInfo();
    }

}
