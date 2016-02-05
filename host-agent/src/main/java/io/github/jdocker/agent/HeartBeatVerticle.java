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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.apache.tamaya.ConfigurationProvider;
import io.github.jdocker.common.ServiceContextManager;

/**
 * Verticle for pulishing heartbeats on the vertx event bus.
 */
public class HeartBeatVerticle extends AbstractVerticle{

    private static final long DEFAULT_HEARTBEAT_INTERVAL_MS = 10000L;
    private long timerID;

    @Override
    public void start() throws Exception {
        Long heartbeatInternal = ConfigurationProvider.getConfiguration().get("jdocker.Docker.Agent.heartbeatInterval", Long.class);
        if (heartbeatInternal == null) {
            heartbeatInternal = Long.valueOf(DEFAULT_HEARTBEAT_INTERVAL_MS);
        }
        timerID = vertx.setTimer(heartbeatInternal, id -> {
            // register node into known nodes
            EventBus bus = vertx.eventBus();
            bus.send("jdocker.docker-agent:heartbeat", createHeartbeat());
        });
    }

    @Override
    public void stop() throws Exception {
        vertx.cancelTimer(timerID);
    }

    private JsonObject createHeartbeat(){
        DockerAgent agent = ServiceContextManager.getServiceContext().getService(DockerAgent.class);
        JsonObject o = new JsonObject()
                .put("component", "docker-agent")
                .put("class", agent.getClass().getName())
                .put("uri", agent.getURI())
                .put("name", agent.getAgentName())
                .put("state", agent.getStatus());
//        switch(agent.getStatus()){
//            case Running:
//                o.put("started", agent.getStarted());
//                break;
//            case Stopped:
//                o.put("stopped", agent.getStopped());
//                break;
//            case Failed:
//                o.put("failed", agent.getFailed());
//                break;
//        }
        return o;
    }
}
