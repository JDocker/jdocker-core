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
package io.github.jdocker.agent;

import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;

/**
 * Created by atsticks on 24.01.16.
 */
public class AgentLauncher extends Launcher{

    public static void main(String... args){
        Launcher.main(new String[]{"run", DockerAgentVerticle.class.getName()});
    }

    public void beforeStartingVertx(VertxOptions options) {
        // read machine IP
        // check for installation of docker, docker-machine and calico
    }

    @Override
    public String getMainVerticle() {
        return DockerAgentVerticle.class.getName();
    }
}
