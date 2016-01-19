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
import io.github.jdocker.ContainerManager;
import io.github.jdocker.ContainerNode;

import java.util.*;
import java.util.logging.Logger;

/**
 * Class describing a deployment action.
 */
public class Deployment {
    private static final Logger LOG = Logger.getLogger(Deployment.class.getName());

    private String id = UUID.randomUUID().toString();
    private List<ContainerRequest> requests = new ArrayList<>();

    /**
     * Instantiates a new Container request.
     */
    public Deployment(Collection<ContainerRequest> requests){
        this.requests.addAll(Objects.requireNonNull(requests));
        for(ContainerRequest req:requests){
            req.getContainerConfig().labels().put("deployment", id);
        }
    }

    Deployment(DeploymentBuilder deploymentBuilder) {
        this.requests.addAll(Objects.requireNonNull(deploymentBuilder.requests));
    }

    public String getId(){
        return id;
    }

    public List<ContainerRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public Collection<ContainerNode> getInstances() {
        return ContainerManager.getContainersWithLabels("deployment="+id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deployment)) return false;

        Deployment that = (Deployment) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Deployment{" +
                "id="+id+
                ", containerRequests=" + requests +
                '}';
    }

    public ContainerRequest createRequest(ContainerConfig config, int scale){
        ContainerRequest req = new ContainerRequest(config, scale);
        req.scale = scale;
        req.containerConfig = Objects.requireNonNull(config);
        if(scale<0){
            throw new IllegalArgumentException("Scale must be >= 0.");
        }
        return req;
    }

    public final class ContainerRequest{
        private int scale;
        private ContainerConfig containerConfig;

        ContainerRequest(ContainerConfig config){
            this(config, 1);
        }

        ContainerRequest(ContainerConfig config, int scale){
            this.scale = scale;
            this.containerConfig = Objects.requireNonNull(config);
            if(scale<0){
                throw new IllegalArgumentException("Scale must be >= 0.");
            }
        }

        public Deployment getDeployment(){
            return Deployment.this;
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

}
