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
package io.github.jdocker.deployment;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * Class describing a deployment action.
 */
public class Deployment {
    private static final Logger LOG = Logger.getLogger(Deployment.class.getName());
    private int targetScale = 0;
    private Set<String> targets = new TreeSet<>();
    private ContainerConfig containerConfig;
    private List<ContainerInfo> containers = new ArrayList<>();

    /**
     * Instantiates a new Container request.
     */
    public Deployment(ContainerConfig containerConfig, int targetScale, Set<String> targets){
        this.containerConfig = Objects.requireNonNull(containerConfig);
        this.targetScale = targetScale;
        this.targets.addAll(targets);
    }

    public void addContainer(ContainerInfo container)   {
        this.containers.add(Objects.requireNonNull(container));
    }

    public void removeContainer(ContainerInfo container){
        this.containers.remove(container);
    }

    public List<ContainerInfo> getContainers() {
        return new ArrayList<>(containers);
    }

    public Set<String> getTargets(){
        return targets;
    }

    public int getTargetScale(){
        return targetScale;
    }

    public int getPlannedScale(){
        return this.containers.size();
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deployment)) return false;

        Deployment that = (Deployment) o;

        return getContainerConfig().equals(that.getContainerConfig());

    }

    @Override
    public int hashCode() {
        return containerConfig.hashCode();
    }

    @Override
    public String toString() {
        return "Deployment{" +
                "containers=" + containers +
                ", containerConfig=" + containerConfig +
                '}';
    }
}
