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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atsticks on 19.01.16.
 */
public class DeploymentBuilder {

    List<ContainerRequest> requests = new ArrayList<>();

    public DeploymentBuilder addRequest(ContainerRequest request) {
        this.requests.add(request);
        return this;
    }

    public DeploymentBuilder addRequest(ContainerConfig config, int scale) {
        this.requests.add(ContainerRequest.of(config, scale));
        return this;
    }

    public DeploymentBuilder addRequest(ContainerConfig config) {
        this.requests.add(ContainerRequest.of(config, 1));
        return this;
    }

    public Deployment build(){
        return new Deployment(this);
    }
}
