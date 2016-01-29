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

import io.github.jdocker.common.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Simple health check that pings an endpoint host.
 */
public class EndpoingPingCheck implements HealthCheck{

    private Endpoint endpoint;
    private String id = UUID.randomUUID().toString();

    public EndpoingPingCheck(Endpoint endpoint){
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    public String getId(){
        return id;
    }

    @Override
    public HealthCheckResult check() {
        HealthCheckResult result = new HealthCheckResult()
                .setInstance("Endpoint:"+endpoint.getName())
                .setShortDesc("Pings the endpoint address.");
        String resv= Executor.execute("ping -c 2 " + endpoint.getHost());
        result.setMessage(resv);
        if(resv.contains("2 packets transmitted, 2 received")){
            result.setResult(CheckResult.SUCCESS);
        }else{
            result.setResult(CheckResult.FAILED);
        }
        return result;
    }

    @Override
    public String toString() {
        return "EndpoingPingCheck{" +
                "endpoint=" + endpoint +
                '}';
    }
}
