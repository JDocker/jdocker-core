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
package io.github.jdocker.events;

import com.spotify.docker.client.messages.ContainerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * DeploymentRequest, distributed as payload within a JDockerEvent.
 */
public final class ContainerRequest {
    private String swarmId;
    private int scale;
    private ContainerConfig containerConfig;
    private Map<String,String> environment = new HashMap<>();

    ContainerRequest(ContainerConfig containerConfig, int scale, String swarmId, Map<String,String> environment) {
        this.containerConfig = Objects.requireNonNull(containerConfig);
        if (scale < 0) {
            throw new IllegalArgumentException("Scale must be >= 0.");
        }
        this.scale = scale;
        this.environment.putAll(environment);
        this.swarmId = swarmId;
    }

    public String getSwarmId(){
        return swarmId;
    }

    public int getScale() {
        return scale;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public Map<String,String> getEnvironment(){
        return environment;
    }

    @Override
    public String toString() {
        return "DeploymentRequest{" +
                "scale=" + scale +
                ", container=" + containerConfig +
                ", swarmId=" + swarmId +
                ", environment=" + environment +
                '}';
    }
}
