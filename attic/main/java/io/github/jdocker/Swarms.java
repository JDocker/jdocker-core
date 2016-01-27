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
package io.github.jdocker;

import io.github.jdocker.common.Executor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class to perform advanced topics such as setting up a complete swarm just with a few machines or containers
 * ready for use.
 */
public final class Swarms {

    private static final Map<String,String> swarms = new ConcurrentHashMap<>();

    private Swarms(){}

    public static String createDiscoveryToken(){
        return Executor.execute("curl -s -XPOST https://discovery-stage.hub.docker.com/v1/clusters");
    }

    /**
     * Creates a swarm cluster with the given hosts.
     * @param machines the machines to be added as swarm nodes.
     * @param masters the machines to be added as swarm masters.
     * @return the swarm discovery id.
     */
    public static String setupSwarmCluster(String name, Collection<UnpooledMachine> machines, Collection<UnpooledMachine> masters){
        String discoveryId = createDiscoveryToken();
        setupSwarmCluster(name, machines, masters, discoveryId);
        Swarms.swarms.put(name, discoveryId);
        return discoveryId;
    }

    /**
     * Creates a swarm cluster with the given hosts.
     * @param machines the machines to be added as swarm nodes.
     * @param masters the machines to be added as swarm masters.
     * @return the swarm discovery id.
     */
    public static void setupSwarmCluster(String name, Collection<UnpooledMachine> machines, Collection<UnpooledMachine> masters,
            String discoveryId){
        // TODO
        Swarms.swarms.put(name, discoveryId);
    }

    /**
     * Adds the given nodes to a swarm cluster.
     * @param machines the machines to be added as swarm nodes.
     * @param masters the machines to be added as swarm masters.
     * @return the swarm discovery id.
     */
    public static void addNodesToSwarmCluster(Collection<UnpooledMachine> machines, Collection<UnpooledMachine> masters,
                                         String discoveryId){
        // TODO
    }

    /**
     * Get the discovery id for a swarm.
     * @param name the swarm name, not null.
     * @return the discovery id, or null.
     */
    public String getDiscoveryId(String name){
        return Swarms.swarms.get(name);
    }

    /**
     * Get the current defined swarm, the values are the discovery keys.
     * @return the swarms, by swarm names.
     */
    public static Map<String,String> getSwarms(){
       return Collections.unmodifiableMap(swarms);
    }


}
