/*
 * Copyright (c) 2014 Spotify AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package io.github.jdocker.serviceregistry;

import io.github.jdocker.common.Endpoint;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A list of endpoints to for service registration.
 */
public class ServiceRegistry extends AbstractVerticle {
    private boolean clustered;
    private final String REG_NAME = "services.shared";
    private final String REG_ADDRESS_PUT = "services.shared.put";
    private final String REG_ADDRESS_GET = "services.shared.get";

    public void start(Future<Void> startFuture) throws Exception {
        clustered = config().getBoolean("clustered", false);
        EventBus eb = vertx.eventBus();

        eb.consumer(REG_ADDRESS_GET, (Handler<Message<String>>) message -> {
            System.out.println("I have received a message: " + message.body());
            String endpointName = message.body();
            findEndpointAndReply(endpointName, message);


        });

        eb.consumer(REG_ADDRESS_PUT, (Handler<Message<String>>) message -> {
            System.out.println("I have received a message: " + message.body());
            Endpoint endpoint = Json.decodeValue(message.body(), Endpoint.class);
            registerEndpoint(endpoint);
        });

        startFuture.complete();
    }

    private void findEndpointAndReply(String endpointName, Message<String> message) {
        SharedData sd = vertx.sharedData();
        if (clustered) {
            findEndpointInClusteredMapAndReply(endpointName, message, sd);
        } else {
            findEndpointEndReply(endpointName, message, sd);
        }
    }

    private void findEndpointEndReply(String endpointName, Message<String> message, SharedData sd) {
        LocalMap<String, String> map1 = sd.getLocalMap(REG_NAME);
        final String endpointValue = map1.get(endpointName);
        // TODO should we define any timeouts or anything else?
        message.reply(endpointValue);
    }

    private void findEndpointInClusteredMapAndReply(String endpointName, Message<String> message, SharedData sd) {
        sd.<String, String>getClusterWideMap(REG_NAME, res -> {
            if (res.succeeded()) {
                AsyncMap<String, String> map = res.result();
                map.get(endpointName, result -> {
                    if (result.succeeded()) {
                        final String endpointValue = result.result();
                        // TODO should we define any timeouts or anything else?
                        message.reply(endpointValue);
                    }
                });
            } else {
                // Something went wrong!
            }
        });
    }

    private void registerEndpoint(Endpoint endpoin) {
        SharedData sd = vertx.sharedData();
        if (clustered) {
            putToClusteredMap(endpoin, sd);
        } else {
            putToMap(endpoin, sd);
        }
    }

    private void putToMap(Endpoint endpoin, SharedData sd) {
        LocalMap<String, String> map1 = sd.getLocalMap(REG_NAME);
        map1.putIfAbsent(endpoin.getName(), Json.encode(endpoin));
    }

    private void putToClusteredMap(Endpoint endpoin, SharedData sd) {
        sd.<String, String>getClusterWideMap(REG_NAME, res -> {
            if (res.succeeded()) {
                AsyncMap<String, String> map = res.result();
                map.putIfAbsent(endpoin.getName(), Json.encode(endpoin), result -> {
                    if (result.succeeded()) {

                    } else {
                        // Something went wrong!
                    }
                });
            } else {
                // Something went wrong!
            }
        });
    }



    private final Map<String, List<Endpoint>> services = new ConcurrentHashMap<>();

    @Deprecated // this impl. won't work in Vert.x
    public List<Endpoint> getEndpoints(String service) {
        Configuration config = ConfigurationProvider.getConfiguration();
        String serviceRefs = config.get("jdocker.services");
        for (String endpointRef : serviceRefs.split(",")) {
            for (String endpointName : endpointRef.split(",")) {
                endpointName = endpointName.trim();
                String endpointDef = config.get("jdocker.endpoint." + endpointName);
                if (endpointDef != null) {
                    URI endpointURI = null; // protocol:/host_port/domain?tags=a,b,c,d&healthCheck=checkId
                    try {
                        endpointURI = new URI(endpointDef);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Endpoint ep = new Endpoint(endpointName, endpointURI);
                }
            }
        }
        List<Endpoint> endpoints = services.get(service);
        if (endpoints == null) {
            return Collections.emptyList();
        }
        return services.get(service);
    }


    public Set<String> getServices() {
        return services.keySet();
    }

    public static void main(String[] args) {
        // DeploymentOptions options = new DeploymentOptions().setInstances(1).setConfig(new JsonObject().put("host","localhost"));
        // Vertx.vertx().deployVerticle(ServiceRegistry.class.getName(),options);

        VertxOptions vOpts = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(1);
        vOpts.setClustered(true);
        Vertx.clusteredVertx(vOpts, cluster -> {
            if (cluster.succeeded()) {
                final Vertx result = cluster.result();
                result.deployVerticle(ServiceRegistry.class.getName(), options, handle -> {

                });
            }
        });
    }

}
