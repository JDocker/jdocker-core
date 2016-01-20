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

import io.github.jdocker.DockerMachine;

import java.util.Collection;

/**
 * Class that defines which docker nodes can be used for this deployment.
 */
public interface DockerNodeElector {

    /**
     * Evaluates the possible deployment targets. This does a selection from all known nodes in the system that
     * match the deployment's configuration. It is the responsibility of the {@link DockerNodeSelector} to
     * effectively define the effective deployment targets called for deployment.
     * @param request the deployment, not null
     * @return the collection of eligible nodes, never null.
     */
    Collection<DockerMachine> evaluateTargetNodes(ContainerRequest request);


}
