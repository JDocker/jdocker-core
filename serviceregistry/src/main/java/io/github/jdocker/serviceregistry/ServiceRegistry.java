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

  public void start(Future<Void> startFuture) throws Exception {
    clustered = config().getBoolean("clustered", false);

    startFuture.complete();
  }


  private void initRegistrationEndpoint() {

  }

  private void initClusteredStore() {

  }

  private void initLocalStore() {

  }

  private final Map<String, List<Endpoint>> services = new ConcurrentHashMap<>();

  public List<Endpoint> getEndpoints(String service) {
    Configuration config = ConfigurationProvider.getConfiguration();
    String serviceRefs = config.get("jdocker.services");
    for(String endpointRef: serviceRefs.split(",")){
      for(String endpointName: endpointRef.split(",")){
        endpointName = endpointName.trim();
        String endpointDef = config.get("jdocker.endpoint."+endpointName);
        if(endpointDef!=null) {
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
