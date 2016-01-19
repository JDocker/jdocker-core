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

import io.github.jdocker.DockerNode;

import java.util.Collection;
import java.util.Map;

/**
 * Instances implementing this interface are responsible for defining the effective nodes that will be called for
 * deployment.
 */
public interface DockerNodeSelector {

    /**
     * Select the effective target nodes from the eligible nodes.
     * @param electedNodes the node set from where to choose the best matching nodes, e.g. based on existing usage and
     *                     load, or other runtime characteristics.
     * @param request the concrete deployment subrequest.
     * @return the set of nodes to be called for deployment, never null. If the number of nodes does not match the
     * requested {@link io.github.jdocker.deployment.Deployment.ContainerRequest#getScale()} a warning must be logged.
     */
    Collection<DockerNode> selectTargetNodes(Collection<DockerNode> electedNodes,
                                             Deployment.ContainerRequest request);
    
}
