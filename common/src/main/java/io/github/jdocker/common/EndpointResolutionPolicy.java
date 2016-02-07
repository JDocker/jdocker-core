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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Policy used to determine which service to call. Can be used for load balancing, auth issues and more.
 */
public interface EndpointResolutionPolicy {

    /**
     * Selects one endpoint from the list of endpoints and returns a corresponing instance.
     *
     * @param endpoints the endpoints available.
     * @return the endpoint to be used, or null, if no one is eligible.
     */
    Endpoint resolve(Collection<Endpoint> endpoints);

    /**
     * Resolution poicy than randomly selects an instance. If the collection is empty, null is returned.
     */
    EndpointResolutionPolicy RANDOM_RESOLUTIONPOLICY = l ->
        {
            if(l.isEmpty()){
                return null;
            }
            int index = new Random().nextInt(l.size()-1);
            return new ArrayList<>(l).get(index);
        };
}
