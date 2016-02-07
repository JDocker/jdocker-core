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

import java.util.Arrays;
import java.util.Collection;

/**
 * Factory that allows to access Java APIs for calling services. Corresponding instances of
 * {@code ServiceAccessorFactorySpi} must be registered for every service being accessed.
 */
public interface ServiceFactory {


    /**
     * Creates/returns a Java API accessor based on the registered accessor factory SPIs using the default
     * resolution policy (mostly round robin).
     * @param endpoints the endpoints
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    <T> T getStaticServiceProxy(Class<T> serviceType, EndpointResolutionPolicy endpointResolutionPolicy, Collection<Endpoint> endpoints);

    /**
     * Creates a new accessor for the given service, the effective endpoints are looked up dynamically from
     * the {@link ServiceDiscovery}.
     * @param serviceType the target type to be created.
     * @param serviceName the service name
     * @param endpointResolutionPolicy the resolution policy
     * @param tags the tags for filtering the result
     * @param <T> the type
     * @return a proxy instance of the given type T, not null.
     * @throws IllegalStateException if no such proxy can be created.
     */
    <T> T getService(Class<T> serviceType, String serviceName, Collection<String> tags,
                                     EndpointResolutionPolicy endpointResolutionPolicy);

    /**
     * Creates a new accessor for the given service, the effective endpoints are looked up dynamically from
     * the {@link ServiceDiscovery}.
     * @param serviceType the target type to be created. The service name is derived from the fully qualified type name.
     * @param endpointResolutionPolicy the resolution policy
     * @param tags the tags for filtering the result
     * @param <T> the type
     * @return a proxy instance of the given type T, not null.
     * @throws IllegalStateException if no such proxy can be created.
     */
    default <T> T getService(Class<T> serviceType, Collection<String> tags,
                     EndpointResolutionPolicy endpointResolutionPolicy){
        return getService(serviceType, serviceType.getName(), tags, endpointResolutionPolicy);
    }

    /**
     * Creates a new accessor for the given service, the effective endpoints are looked up dynamically from
     * the {@link ServiceDiscovery}. Hereby the random resolution policy is applied.
     * @param serviceType the target type to be created. The service name is derived from the fully qualified type name.
     * @param tags the tags for filtering the result
     * @param <T> the type
     * @return a proxy instance of the given type T, not null.
     * @throws IllegalStateException if no such proxy can be created.
     */
    default <T> T getService(Class<T> serviceType, Collection<String> tags){
        return getService(serviceType, serviceType.getName(), tags, EndpointResolutionPolicy.RANDOM_RESOLUTIONPOLICY);
    }

    /**
     * Creates/returns a Java API accessor based on the registered accessor factory SPIs using the default
     * resolution policy (random).
     * @param endpoints the endpoints
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    default <T> T getStaticServiceProxy(Class<T> serviceType, Collection<Endpoint> endpoints){
        return getStaticServiceProxy(serviceType, EndpointResolutionPolicy.RANDOM_RESOLUTIONPOLICY, endpoints);
    }

    /**
     * Creates/returns a Java API accessor based on the registered accessor factory SPIs.
     * @param endpoints a given number of endpoints
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    default <T> T getStaticServiceProxy(Class<T> serviceType, Endpoint... endpoints){
        return getStaticServiceProxy(serviceType, Arrays.asList(endpoints));
    }

    /**
     * Creates/returns a Java API accessor based on the registered accessor factory SPIs.
     * @param endpoints a given number of endpoints
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    default <T> T getStaticServiceProxy(Class<T> serviceType, EndpointResolutionPolicy endpointResolutionPolicy,
                                        Endpoint... endpoints){
        return getStaticServiceProxy(serviceType, endpointResolutionPolicy, Arrays.asList(endpoints));
    }

    /**
     * Creates/returns a dynamic Java API accessor based on the registered accessor factory SPIs using the default
     * resolution policy (mostly round robin). Dynamic accessor look up the current available resources in the
     * {@link ServiceDiscovery} instead of relying on a static list of resources.
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    default <T> T getService(Class<T> serviceType, String serviceName, Collection<String> tags){
        return getService(serviceType, serviceName, tags, EndpointResolutionPolicy.RANDOM_RESOLUTIONPOLICY);
    }

    /**
     * Creates a new accessor for the given service, the effective endpoints are looked up dynamically from
     * the {@link ServiceDiscovery}, uses a random resolution policy.
     * @param serviceType the target type to be created.
     * @param serviceName the service name
     * @param tags the tags for filtering the result
     * @param <T> the type
     * @return a proxy instance of the given type T, not null.
     * @throws IllegalStateException if no such proxy can be created.
     */
    default <T> T getService(Class<T> serviceType, String serviceName, String... tags){
        return getService(serviceType, serviceName, Arrays.asList(tags));
    }

}
