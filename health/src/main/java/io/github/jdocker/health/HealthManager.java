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
package io.github.jdocker.health;

import io.github.jdocker.common.HealthCheck;
import io.vertx.core.AbstractVerticle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A HealthManager instance. There are multiple health managers in the system. Normally one per docker host is
 * automatically setup along the jdocker agent.
 */
public class HealthManager extends AbstractVerticle{

    /** The uniwue name of the health manager. */
    private String name;
    /* The currently contained health checks. */
    private Map<String,HealthCheck> healthChecks = new ConcurrentHashMap<>();





    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return name;
    }

    /**
     * Get health check health check.
     *
     * @param id the id
     * @return the health check
     */
    public HealthCheck getHealthCheck(String id){
        return healthChecks.get(id);
    }

    /**
     * Remove health check health check.
     *
     * @param id the id
     * @return the health check
     */
    public HealthCheck removeHealthCheck(String id){
        return healthChecks.remove(id);
    }

    /**
     * Get health checks collection.
     *
     * @return the collection
     */
    public Collection<HealthCheck> getHealthChecks(){
        return Collections.unmodifiableCollection(healthChecks.values());
    }

}
