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

import java.util.Collection;
import java.util.Set;

/**
 * Created by atsticks on 19.01.16.
 */
public interface IPAddressManager {

    /**
     * Create a network to be used by containers with complete visibility.
     * @param name the network name, not null.
     * @return the command output.
     */
    String createNetwork(String name);

    /**
     * Remove the defined network.
     * @param name the network's name, not null.
     * @return the command output.
     */
    String removeNetwork(String name);

    /**
     * Get the currently defined network ids.
     * @return the network ids.
     */
    Set<String> getNetworks();

    /**
     * Get the adddress pool by name.
     * @param name the pool name, not null
     * @return the pool, or null.
     */
    AddressPool getAddressPool(String name);

    /**
     * Get the asigned network address pools.
     * @return the network address pool, or null, if no such networks exists.
     */
    Collection<AddressPool> getAddressPools();

    /**
     * This command adds all IP addresses between two IPs as Calico pool(s).<br/>
     * NOTE: Calico pools must be identified with a CIDR prefix, so in the case that the start and end of the range
     * are not on a single CIDR boundary, this command creates multiple pools such that the entire range is covered.<br/>
     * @param pool the ip address pool.
//     * @param network the network id.
     * @return the command output.
     */
    void addAddressPool(AddressPool pool);

    /**
     * This command is used to remove configured CIDR pools.
     * @param pool The pool to be removed.
     */
    void removeAddressPool(AddressPool pool);
    /**
     * This command allows you to release an IP address that had been previously assigned to an endpoint. When an IP
     * address is released, it becomes available for assignment to any endpoint.<br/>
     * Note that this does not remove the IP from any existing endpoints that may be using it, so we generally
     * recommend you use this command to clean up addresses from endpoints that were not cleanly removed from Calico.<nr/>
     * This command can be run on any Calico node.
     * @param ip the ip address, not null
     * @return the command output
     */
    String releaseIP(String ip);

    /**
     * This command prints information about a given IP address, such as special attributes defined for the IP or
     * whether the IP has been reserved by a user.<br/>
     * @param ip the ip address, not null
     * @return the info text
     */
    String getIPInfo(String ip);

    /**
     * Access general info about the known address pools.
     * @return a general info on the current pools, not null.
     */
    String getAddressPoolInfo();
}
