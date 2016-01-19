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
package io.github.jdocker.deployment.internal;

import com.spotify.docker.client.messages.ContainerCreation;
import io.github.jdocker.ContainerManager;
import io.github.jdocker.ContainerNode;
import io.github.jdocker.DockerNode;
import io.github.jdocker.deployment.Deployment;
import io.github.jdocker.deployment.DockerNodeElector;
import io.github.jdocker.deployment.DockerNodeSelector;
import io.github.jdocker.spi.DeployerSpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the deployment of a new container.
 */
public final class DefaultDeployerSpi implements DeployerSpi{

    private static final Logger LOG = Logger.getLogger(DefaultDeployerSpi.class.getName());

    @Override
    public Collection<DockerNode> getEligibleNodes(Deployment.ContainerRequest request) {
        return ServiceContextManager.getServiceContext().getService(DockerNodeElector.class).evaluateTargetNodes(
                request
        );
    }

    @Override
    public List<ContainerCreation> deploy(Deployment deployment) {
        List<ContainerCreation> nodesDeployed = new ArrayList<>();
        for(Deployment.ContainerRequest req: deployment.getRequests()) {
            Collection<DockerNode> nodes = getEligibleNodes(req);
            nodes = ServiceContextManager.getServiceContext().getService(DockerNodeSelector.class).selectTargetNodes(
                    nodes, req);
            for (DockerNode node : nodes) {
                nodesDeployed.add(deployDirect(node, req));
            }
        }
        return nodesDeployed;
    }

    @Override
    public void ensureScale(Deployment deployment) {
        Collection<ContainerNode> deployedNodes = deployment.getInstances();
        Map<Deployment.ContainerRequest, List<ContainerNode>> detailedDeployments = new HashMap<>();
        for(ContainerNode node: deployedNodes) {
            for (Deployment.ContainerRequest req : deployment.getRequests()) {
                if (req.getContainerConfig().image().equals(node.getInstance().image())) {
                    List<ContainerNode> list = detailedDeployments.get(req);
                    if(list==null){
                        list = new ArrayList<>();
                        detailedDeployments.put(req, list);
                    }
                   list.add(node);
                }
            }
        }
        for (Deployment.ContainerRequest req : deployment.getRequests()) {
            int diff = req.getScale() - detailedDeployments.get(req).size();
            if(diff==0){
                continue;
            }
            else if(diff<0){
                for(ContainerNode container:detailedDeployments.get(req)){
                    ContainerManager.removeContainer(container);
                    diff++;
                    if(diff>=0){
                        break;
                    }
                }
            }
            else if(diff>0) {
                Collection<DockerNode> nodes = getEligibleNodes(req);
                nodes = ServiceContextManager.getServiceContext().getService(DockerNodeSelector.class).selectTargetNodes(
                        nodes, req);
                for (DockerNode node : nodes) {
                    List<ContainerCreation> c = deployDirect(node, deployment);
                    diff--;
                    if(diff<=0){
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ContainerCreation deployDirect(DockerNode node, Deployment.ContainerRequest request) {
        try {
            return node.getClient().createContainer(request.getContainerConfig());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Container deployment failed for " + request.getContainerConfig().image() +
                    " on node " + node.getName());
            return null;
        }
    }

    @Override
    public List<ContainerCreation> deployDirect(DockerNode node, Deployment deployment) {
        List<ContainerCreation> result = new ArrayList<>();
        for(Deployment.ContainerRequest req: deployment.getRequests()) {
            result.add(deployDirect(node, req));
        }
        return result;
    }

}