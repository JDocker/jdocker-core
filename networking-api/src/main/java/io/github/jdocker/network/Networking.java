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

import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.common.ServiceContextManager;

import java.util.Collection;

/**
 * Singleton for networking functionality.
 */
public final class Networking {

    private static final NetworkManager spi = ServiceContextManager.getServiceContext().getService(NetworkManager.class);

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
     * Checks if the given software is already installed locally.
     */
    public static boolean installNetworkingInstalled(){
        return spi.isNetworkingInstalled();
    }

    /**
     * Installs all needed required to run networking with the given machine.
     */
    public static void installNetworking(){
        spi.installNetworking();
    }

    /**
     * Stops networking services.
     */
    public static void stopNetworking(){
        spi.stopNetworking();
    }

    /**
     * Starts networking services.
     */
    public static void startNetworking(){
        spi.startNetworking();
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

}
