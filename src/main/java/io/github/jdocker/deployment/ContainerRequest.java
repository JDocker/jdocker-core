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

import java.util.Objects;

/**
 * Created by atsticks on 20.01.16.
 */
public final class ContainerRequest {
    private Deployment deployment;
    private int scale;
    private ContainerConfig containerConfig;

    ContainerRequest(ContainerConfig config) {
        this.deployment = Objects.requireNonNull(deployment);
        this.scale =1;
    }

    ContainerRequest(Deployment deployment, ContainerConfig config, int scale) {
        this.deployment = Objects.requireNonNull(deployment);
        this.containerConfig = Objects.requireNonNull(config);
        if (scale < 0) {
            throw new IllegalArgumentException("Scale must be >= 0.");
        }
        this.scale = scale;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public int getScale() {
        return scale;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    @Override
    public String toString() {
        return "ContainerRequest{" +
                "scale=" + scale +
                ", containerConfig=" + containerConfig +
                '}';
    }
}
