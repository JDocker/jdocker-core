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

import com.spotify.docker.client.messages.ContainerCreation;
import io.github.jdocker.ContainerManager;
import io.github.jdocker.ContainerNode;
import io.github.jdocker.DockerNode;
import io.github.jdocker.DockerNodeRegistry;
import io.github.jdocker.deployment.internal.DefaultContainer;
import io.github.jdocker.spi.DeployerSpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the deployment of new container.
 */
public final class Deployer {

    private static final Logger LOG = Logger.getLogger(Deployer.class.getName());

    private static final DeployerSpi SPI = ServiceContextManager.getServiceContext().getService(DeployerSpi.class);

    private Deployer(){}

    public static Collection<DockerNode> getEligibleNodes(Deployment deployment) {
        return SPI.getEligibleNodes(deployment);
    }

    public static List<ContainerCreation> deploy(Deployment deployment) {
        return SPI.deploy(deployment);
    }

    public static List<ContainerCreation> ensureScale(Deployment deployment) {
        return SPI.ensureScale(deployment);
    }

    public static ContainerCreation deployDirect(DockerNode node, Deployment deployment) {
        return SPI.deployDirect(node, deployment);
    }

}