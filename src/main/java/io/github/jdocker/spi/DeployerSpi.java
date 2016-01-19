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
package io.github.jdocker.spi;

import com.spotify.docker.client.messages.ContainerCreation;
import io.github.jdocker.DockerNode;
import io.github.jdocker.deployment.Deployment;

import java.util.Collection;
import java.util.List;

/**
 * Class that handles the deployment of new container.
 */
public interface DeployerSpi {

    /**
     * Select the nodes that are eligible for the given deployment.
     * @param deployment the deployment, not null.
     * @return the eligible nodes.
     */
    Collection<DockerNode> getEligibleNodes(Deployment.ContainerRequest request);

    /**
     * Deploy the deployment.
     * @param deployment the target deployment.
     * @return the creational node descriptors of the created containers.
     */
    List<ContainerCreation> deploy(Deployment deployment);

    /**
     * Ensure/check the required scale is in place. If needed additional nodes are created or existing ones are
     * stopped and removed.
     * @param deployment the target deployment
     * @return the creational state of new nodes created.
     */
    void ensureScale(Deployment deployment);

    ContainerCreation deployDirect(DockerNode node, Deployment.ContainerRequest request);

    /**
     * Directly deploy to a specific docker instance.
     * @param node the target docker node.
     * @param deployment the target deployment
     * @return the creational contexts of the newly created node.
     */
    List<ContainerCreation> deployDirect(DockerNode node, Deployment deployment);

}