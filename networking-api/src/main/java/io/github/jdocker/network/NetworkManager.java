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
import io.github.jdocker.DockerHost;
import io.github.jdocker.network.NetworkingStatus;
import io.github.jdocker.network.SecurityProfile;

import java.util.Collection;

/**
 * Created by atsticks on 19.01.16.
 */
public interface NetworkManager {

    /**
     * Access general status of the networking support.
     * @return the current statis,. never null.
     */
    NetworkingStatus getStatus();

    /**
     * Get information about the networking plugin used.
     * @return a version information, not null.
     */
    String getVersion();

    /**
     * Installs all needed required to run networking with the given machine.
     */
    void installNetworking();

    /**
     * Determines if the networking software is locally installed on the machine.
     * @return true, if the required software is installed.
     */
    boolean isNetworkingInstalled();

    /**
     * Stops networking on a given docker machine.
     */
    void stopNetworking();

    /**
     * Starts networking on a given docker machine.
     */
    void startNetworking();

    /**
     * Create a security profile. Security profiles can be reused for several containers.
     * @param profile the security profile, not null.
     */
    void createSecurityProfile(SecurityProfile profile);

    /**
     * Updates the security profile, also affecting running containers.
     * @param profile the security profile.
     */
    void updateSecurityProfile(SecurityProfile profile);

    /**
     * Removes the security profile.
     * @param profile the security profile, not null.
     */
    void removeSecurityProfile(SecurityProfile profile);

    /**
     * Access a security profile by name;
     * @return
     */
    SecurityProfile getSecurityProfile(String name);

    /**
     * Get all assigned security profiles.
     * @param container the target container.
     * @return a list of names of the profiles assigned.
     */
    Collection<String> getSecurityProfiles(ContainerInfo container);

    /**
     * Get all known security profile names.
     * @return a list of names of the profiles known.
     */
    Collection<String> getSecurityProfiles();

    /**
     * Assign security profiles to a container, hereby removing all other profiles on the container.
     * @param container the target container, not null.
     * @param profiles the profiles to be set, not null.
     */
    void setSecurityProfiles(ContainerInfo container, SecurityProfile... profiles);

    /**
     * Adds security profiles to a container.
     * @param container the target container, not null.
     * @param profiles the profiles to be assigned, not null.
     */
    void addSecurityProfiles(ContainerInfo container, SecurityProfile... profiles);

    /**
     * Removes the given security profiles from a container.
     * @param container the target container, not null.
     * @param profiles the profiles to be removed, not null.
     */
    void removeSecurityProfiles(ContainerInfo container, SecurityProfile... profiles);

    /**
     * Access the security profile for a container. In calico the profile id matches the endpoint ID of the container, so
     * each container has a profile attached that, by default, allows everything in and out. To change this create
     * a new {@link io.github.jdocker.network.SecurityProfileBuilder} based on the profile returned, change anything that makes sense and
     * call {@link #updateSecurityProfile(SecurityProfile)} to activate your changes.
     * @return the container's security profile.
     */
    SecurityProfile getSecurityProfile(ContainerInfo container);
}
