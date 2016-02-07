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
package io.github.jdocker.common.internal;

import io.github.jdocker.common.Endpoint;
import io.github.jdocker.common.EndpointResolutionPolicy;
import io.github.jdocker.common.ServiceFactory;
import io.github.jdocker.common.ServiceContextManager;
import io.github.jdocker.common.spi.ServiceAccessorProxyFactorySpi;

import java.util.Collection;

/**
 * Default implementation of the {@link ServiceFactory} hereby using the {@link ServiceAccessorProxyFactorySpi}
 * registered.
 */
public class DefaultServiceFactory implements ServiceFactory {

    @Override
    public <T> T getStaticServiceProxy(Class<T> serviceType, EndpointResolutionPolicy endpointResolutionPolicy, Collection<Endpoint> endpoints) {
        for(ServiceAccessorProxyFactorySpi spi: ServiceContextManager.getServiceContext().getServices(ServiceAccessorProxyFactorySpi.class)){
            T t = spi.createStaticServiceProxy(serviceType, endpointResolutionPolicy, endpoints);
            if(t!=null){
                return t;
            }
        }
        throw new IllegalStateException("Cannot create service accessor for " + serviceType.getName());
    }

    @Override
    public <T> T getService(Class<T> serviceType, String serviceName, Collection<String> tags, EndpointResolutionPolicy endpointResolutionPolicy) {
        for(ServiceAccessorProxyFactorySpi spi: ServiceContextManager.getServiceContext().getServices(ServiceAccessorProxyFactorySpi.class)){
            T t = spi.createServiceProxy(serviceType, serviceName, tags, endpointResolutionPolicy);
            if(t!=null){
                return t;
            }
        }
        throw new IllegalStateException("Cannot create service accessor for " + serviceType.getName());
    }
}
