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
package io.github.jdocker.common.spi;

import io.github.jdocker.common.Endpoint;
import io.github.jdocker.common.EndpointResolutionPolicy;
import io.github.jdocker.common.ServiceDiscovery;

import java.util.Collection;

/**
 * SPI class to allow access of services using Java typed interfaces. Register instances with the ServiceContext.
 */
public interface ServiceAccessorProxyFactorySpi {

    /**
     * Creates/returns a Java API accessor based on the registered accessor factory SPIs using the default
     * resolution policy (mostly round robin).
     * @param endpoints the endpoints
     * @return a Java implementation of the required type, accessing the given endpoint.
     */
    <T> T createStaticServiceProxy(Class<T> serviceType, EndpointResolutionPolicy endpointResolutionPolicy, Collection<Endpoint> endpoints);

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
    <T> T createServiceProxy(Class<T> serviceType, String serviceName, Collection<String> tags,
                     EndpointResolutionPolicy endpointResolutionPolicy);
}
