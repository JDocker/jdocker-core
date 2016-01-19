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
package io.github.jdocker.machine;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import io.github.jdocker.DockerNode;
import io.github.jdocker.DockerNodeRegistry;
import io.github.jdocker.common.Executor;
import io.github.jdocker.common.JSONMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface representing a docker container host.
 */
public interface Machine {

    String getName();

    /** The timestamp, when the docker-machine overall state was refreshed. */
    long lastUpdated = System.currentTimeMillis();

    int getCPUs();

    int getMemory();

    long getDiskSize();

    long getLastUpdated();

    MachineStatus getMachineStatus();

    MachineConfig getConfiguration();

    /**
     * Refreshes the machine's state and data, typically returning the JSON as returned by the docker API, mapped to a
     * key/value table.
     * <pre>
     * {
     "ConfigVersion": 3,
     "Driver": {
     "VBoxManager": {},
     "IPAddress": "192.168.99.103",
     "SSHUser": "docker",
     "SSHPort": 59187,
     "MachineName": "swarm-agent-00",
     "SwarmMaster": false,
     "SwarmHost": "tcp://0.0.0.0:3376",
     "SwarmDiscovery": "token://291c01c426135975e91d9f81b2a276df",
     "StorePath": "/home/atsticks/.docker/docker-machine",
     "CPU": 1,
     "Memory": 1024,
     "DiskSize": 20000,
     "Boot2DockerURL": "",
     "Boot2DockerImportVM": "",
     "HostOnlyCIDR": "192.168.99.1/24",
     "HostOnlyNicType": "82540EM",
     "HostOnlyPromiscMode": "deny",
     "NoShare": false
     },
     "DriverName": "virtualbox",
     "HostOptions": {
     "Driver": "",
     "Memory": 0,
     "Disk": 0,
     "EngineOptions": {
     "ArbitraryFlags": [],
     "Dns": null,
     "GraphDir": "",
     "Env": [],
     "Ipv6": false,
     "InsecureRegistry": [],
     "Labels": [],
     "LogLevel": "",
     "StorageDriver": "",
     "SelinuxEnabled": false,
     "TlsVerify": true,
     "RegistryMirror": [],
     "InstallURL": "https://get.docker.com"
     },
     "SwarmOptions": {
     "IsSwarm": true,
     "Address": "",
     "Discovery": "token://291c01c426135975e91d9f81b2a276df",
     "Master": false,
     "Host": "tcp://0.0.0.0:3376",
     "Image": "swarm:latest",
     "Strategy": "spread",
     "Heartbeat": 0,
     "Overcommit": 0,
     "ArbitraryFlags": []
     },
     "AuthOptions": {
     "CertDir": "/home/atsticks/.docker/docker-machine/certs",
     "CaCertPath": "/home/atsticks/.docker/docker-machine/certs/ca.pem",
     "CaPrivateKeyPath": "/home/atsticks/.docker/docker-machine/certs/ca-key.pem",
     "CaCertRemotePath": "",
     "ServerCertPath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00/server.pem",
     "ServerKeyPath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00/server-key.pem",
     "ClientKeyPath": "/home/atsticks/.docker/docker-machine/certs/key.pem",
     "ServerCertRemotePath": "",
     "ServerKeyRemotePath": "",
     "ClientCertPath": "/home/atsticks/.docker/docker-machine/certs/cert.pem",
     "StorePath": "/home/atsticks/.docker/docker-machine/machines/swarm-agent-00"
     }
     },
     "Name": "swarm-agent-00",
     "RawDriver": "eyJWQm94TWFuYWdlciI6e30sIklQQWRkcmVzcyI6IjE5Mi4xNjguOTkuMTAzIiwiU1NIVXNlciI6ImRvY2tlciIsIlNTSFBvcnQiOjU5MTg3LCJNYWNoaW5lTmFtZSI6InN3YXJtLWFnZW50LTAwIiwiU3dhcm1NYXN0ZXIiOmZhbHNlLCJTd2FybUhvc3QiOiJ0Y3A6Ly8wLjAuMC4wOjMzNzYiLCJTd2FybURpc2NvdmVyeSI6InRva2VuOi8vMjkxYzAxYzQyNjEzNTk3NWU5MWQ5ZjgxYjJhMjc2ZGYiLCJTdG9yZVBhdGgiOiIvaG9tZS9hdHN0aWNrcy8uZG9ja2VyL21hY2hpbmUiLCJDUFUiOjEsIk1lbW9yeSI6MTAyNCwiRGlza1NpemUiOjIwMDAwLCJCb290MkRvY2tlclVSTCI6IiIsIkJvb3QyRG9ja2VySW1wb3J0Vk0iOiIiLCJIb3N0T25seUNJRFIiOiIxOTIuMTY4Ljk5LjEvMjQiLCJIb3N0T25seU5pY1R5cGUiOiI4MjU0MEVNIiwiSG9zdE9ubHlQcm9taXNjTW9kZSI6ImRlbnkiLCJOb1NoYXJlIjpmYWxzZX0="
     }
     * </pre>
     */
    void refresh();

    /**
     * Reloads the environment from DockerNodeRegistry.
     */
    Set<String> readEnvironment();

    /**
     * Inspect a docker-machine's details.
     * @return
     */
    Map<String,String> inspect();

    /**
     * Refresh the docker-machine's status only by calling {@code docker-docker-machine status <NAME>}.
     * @return the docker-machine's status
     */
    MachineStatus refreshStatus();

    /**
     * Restarts a docker-machine.
     */
    void restart();

    /**
     * Regenerates the TLS certificates.
     */
    void regenerateCerts();

    /**
     * Removes a docker-machine.
     */
    void remove();

    /**
     * Removes the docker-machine completely.
     */
    void kill();

    /**
     * Starts the docker-machine.
     */
    void start();

    /**
     * Stops the docker-machine.
     */
    void stop();

    /**
     * Get the docker-machine^s URL.
     * @return
     */
    URI getURL();

    /**
     * Upgrades the docker-machine to the latest version of DockerNodeRegistry.
     */
    void upgrade();

    /**
     * Get the UTC timestamp of the last full update of this docker-machine's state.
     * @return the last update in millis.
     */
    long getLastUpdate();

    DockerClient createDockerClient() throws Exception ;

    /**
     * Registers this machine into the global shared Docker repository accessible from
     * {@link io.github.jdocker.DockerNodeRegistry}.
     */
    DockerNode registerMachineAsDockerNode(String...labels)throws Exception;

    /**
     * Creates the machine and configures it for being eligible as deployment target.
     */
    void createMachine();

}
