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
package io.github.jdocker;


import com.spotify.docker.client.DockerClient;
import io.github.jdocker.spi.DockerNodeRegistrySpi;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.Collection;
import java.util.Set;

/**
 * Registry managing the docker registries known to this master orchestration node.
 */
public class DockerNodeRegistry {

    private static final DockerNodeRegistrySpi SPI = ServiceContextManager.getServiceContext().getService(
            DockerNodeRegistrySpi.class
    );

    private DockerNodeRegistry(){}

    public static DockerNode addDocker(String name, DockerClient client, String... labels){
        return SPI.addDocker(name, client, labels);
    }

    public static DockerNode getDocker(String name){
        return SPI.getDocker(name);
    }

    public static Collection<DockerNode> getDockers(){
        return SPI.getDockers();
    }

    public static Set<String> getDockerNames(){
        return SPI.getDockerNames();
    }

    public static DockerNode removeDocker(String name){
        return SPI.removeDocker(name);
    }

}
