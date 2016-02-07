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
package io.github.jdocker.common;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

/**
 * Component interface for the service discovery mechanism.
 */
public interface ServiceDiscovery {

    /**
     * Selects a random endpoint from the list of known endpoints for the given service and returns it.
     * @param serviceName the service name, not null.
     * @param tags
     * @return an endpoint to be used.
     */
    Collection<Endpoint> getEndpoints(String serviceName, Collection<String> tags);

    /**
     * Selects a random endpoint from the list of known endpoints for the given service and returns it.
     * @param serviceName the service name, not null.
     * @return an endpoint to be used.
     */
    Endpoint getEndpoint(String serviceName, Collection<String> tags);

    /**
     * Creates a new endpoint with the given service name and URI and registers it.
     * @param serviceName service name, not null.
     * @param endpoint the endpoint URI, not null.
     * @return
     */
    Endpoint registerEndpoint(String serviceName, URI endpoint, Collection<String> tags);

    /**
     * Registers the given endpoint.
     * @param endpoint the endpoint, not null.
     */
    void registerEndpoint(Endpoint endpoint);

    /**
     * Removes an endpoint with the given service name and URI.
     * @param serviceName service name, not null.
     * @param endpoint the endpoint URI, not null.
     * @return
     */
    Endpoint removeEndpoint(String serviceName, URI endpoint, Collection<String> tags);

    /**
     * Removes an endpoint.
     * @param endpoint the endpoint, not null.
     */
    void removeEndpoint(Endpoint endpoint);

    /**
     * Get a collection of all currently known endpoint matching the given service name.
     * @param serviceName the service name, not null.
     * @param tags the tags for constraining the collection.
     * @return an endpoint to be used.
     */
    default Collection<Endpoint> getEndpoints(String serviceName, String... tags){
        return getEndpoints(serviceName, Arrays.asList(tags));
    }

    /**
     * Selects a random endpoint from the list of known endpoints for the given service and returns it.
     * @param serviceName the service name, not null.
     * @return an endpoint to be used.
     */
    default Endpoint getEndpoint(String serviceName, String... tags){
        return getEndpoint(serviceName, Arrays.asList(tags));
    }

    /**
     * Creates a new endpoint with the given service name and URI and registers it.
     * @param serviceName service name, not null.
     * @param endpoint the endpoint URI, not null.
     * @return
     */
    default Endpoint registerEndpoint(String serviceName, URI endpoint, String... tags){
        return registerEndpoint(serviceName, endpoint, Arrays.asList(tags));
    }

    /**
     * Removes an endpoint with the given service name and URI.
     * @param serviceName service name, not null.
     * @param endpoint the endpoint URI, not null.
     * @return
     */
    default Endpoint removeEndpoint(String serviceName, URI endpoint, String... tags){
        return removeEndpoint(serviceName, endpoint, Arrays.asList(tags));
    }

}
