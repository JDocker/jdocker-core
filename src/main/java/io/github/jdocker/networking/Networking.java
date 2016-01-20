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

import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.JDockerMachine;
import io.github.jdocker.spi.NetworkingIPAMSpi;
import io.github.jdocker.spi.NetworkingSpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.Collection;
import java.util.Set;

/**
 * Singleton for networking functionality.
 */
public final class Networking {

    private static final NetworkingSpi spi = ServiceContextManager.getServiceContext().getService(NetworkingSpi.class);
    private static final NetworkingIPAMSpi ipamSpi = ServiceContextManager.getServiceContext().getService(NetworkingIPAMSpi.class);

    private Networking(){}

    /**
     * Get the current
     * @return the simple name of the registered SPI.
     */
    public static String getNetworkDriver(){
        return spi.getClass().getSimpleName();
    }

    /**
     * Access general status of the networking support.
     * @return the current statis,. never null.
     */
    public static NetworkingStatus getStatus(){
        return spi.getStatus();
    }

    /**
     * Get information about the networking plugin used.
     * @return a version information, not null.
     */
    public static String getVersion(){
        return spi.getVersion();
    }

    /**
     * Installs all needed required to run networking with the given machine.
     * @param machine the target machine, not null.
     */
    public static void installNetworking(JDockerMachine machine){
        spi.installNetworking(machine);
    }

    /**
     * Stops networking on a given docker machine.
     * @param machine the machine, not null.
     */
    public static void stopNetworking(JDockerMachine machine){
        spi.stopNetworking(machine);
    }

    /**
     * Starts networking on a given docker machine.
     * @param machine the machine, not null.
     */
    public static void startNetworking(JDockerMachine machine){
        spi.startNetworking(machine);
    }

    /**
     * Create a security profile. Security profiles can be reused for several containers.
     * @param profile the security profile, not null.
     */
    public static void createSecurityProfile(SecurityProfile profile){
        spi.createSecurityProfile(profile);
    }

    /**
     * Updates the security profile, also affecting running containers.
     * @param profile the security profile.
     */
    public static void updateSecurityProfile(SecurityProfile profile){
        spi.updateSecurityProfile(profile);
    }

    /**
     * Removes the security profile.
     * @param profile the security profile, not null.
     */
    public static void removeSecurityProfile(SecurityProfile profile){
        spi.removeSecurityProfile(profile);
    }

    /**
     * Access a security profile by name;
     * @return
     */
    public static SecurityProfile getSecurityProfile(String name){
        return spi.getSecurityProfile(name);
    }

    /**
     * Access the security profile for a container. In calico the profile id matches the endpoint ID of the container, so
     * each container has a profile attached that, by default, allows everything in and out. To change this create
     * a new {@link SecurityProfileBuilder} based on the profile returned, change anything that makes sense and
     * call {@link #updateSecurityProfile(SecurityProfile)} to activate your changes.
     * @return the container's security profile.
     */
    public static SecurityProfile getSecurityProfile(ContainerInfo container){
        return spi.getSecurityProfile(container);
    }

    /**
     * Get all assigned security profiles.
     * @param container the target container.
     * @return a list of names of the profiles assigned.
     */
    public static Collection<String> getSecurityProfiles(ContainerInfo container){
        return spi.getSecurityProfiles(container);
    }

    /**
     * Get all known security profile names.
     * @return a list of names of the profiles known.
     */
    public static Collection<String> getSecurityProfiles(){
        return spi.getSecurityProfiles();
    }

    /**
     * Assign security profiles to a container, hereby removing all other profiles on the container.
     * @param container the target container, not null.
     * @param profiles the profiles to be set, not null.
     */
    public static void setSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        spi.setSecurityProfiles(container, profiles);
    }

    /**
     * Adds security profiles to a container.
     * @param container the target container, not null.
     * @param profiles the profiles to be assigned, not null.
     */
    public static void addSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        spi.addSecurityProfiles(container, profiles);
    }

    /**
     * Removes the given security profiles from a container.
     * @param container the target container, not null.
     * @param profiles the profiles to be removed, not null.
     */
    public static void removeSecurityProfiles(ContainerInfo container, SecurityProfile... profiles){
        spi.removeSecurityProfiles(container, profiles);
    }

    /**
     * Create a network to be used by containers with complete visibility.
     * @param name the network name, not null.
     */
    public static void createNetwork(String name){
        ipamSpi.createNetwork(name);
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
    public static void addAddressPool(AddressPool pool){
        ipamSpi.addAddressPool(pool);

    }

    /**
     * This command is used to remove configured CIDR pools.
     * @param pool The pool to be removed.
     */
    public static void removeAddressPool(AddressPool pool){
        ipamSpi.removeAddressPool(pool);
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
