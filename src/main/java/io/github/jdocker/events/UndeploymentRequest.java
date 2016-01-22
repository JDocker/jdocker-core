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

import com.spotify.docker.client.messages.ContainerInfo;

import java.util.Objects;

/**
 * DeploymentRequest, distributed as payload within a JDockerEvent.
 */
public final class UndeploymentRequest {
    private String swarmId;
    private String containerId;
    private ContainerInfo containerInfo;

    UndeploymentRequest(String containerId, String swarmId) {
        this.containerId = Objects.requireNonNull(containerId);
        this.swarmId = Objects.requireNonNull(swarmId);
    }

    public String getContainerId() {
        return containerId;
    }

    public String getSwarmId(){
        return swarmId;
    }

    @Override
    public String toString() {
        return "ContainerRequest{" +
                "containerId=" + containerId +
                ", swarmId=" + swarmId +
                ", containerInfo=" + containerInfo +
                '}';
    }
}
