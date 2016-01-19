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

import com.spotify.docker.client.DockerClient;
import io.github.jdocker.DockerNode;

import java.util.Collection;
import java.util.Set;

/**
 * Created by atsticks on 19.01.16.
 */
public interface DockerNodeRegistrySpi {

    DockerNode addDocker(String name, DockerClient client, String... labels);

    DockerNode getDocker(String name);

    Collection<DockerNode> getDockers();

    Set<String> getDockerNames();

    DockerNode removeDocker(String name);
}
