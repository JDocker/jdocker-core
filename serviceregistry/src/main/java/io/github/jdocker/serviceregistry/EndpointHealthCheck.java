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

import com.google.common.base.MoreObjects;
import io.github.jdocker.common.CheckResult;
import io.github.jdocker.common.Endpoint;
import io.github.jdocker.common.HealthCheck;
import io.github.jdocker.common.HealthCheckResult;

import java.util.Objects;

/**
 * Simple HealthCheck for an endpoint.
 */
public class EndpointHealthCheck implements HealthCheck {

  private final Endpoint endpoint;

  public EndpointHealthCheck(Endpoint endpoint) {
    this.endpoint = Objects.requireNonNull(endpoint);
  }

  public Endpoint getEndpoint() {
    return endpoint;
  }

  @Override
  public HealthCheckResult check() {
    // TODO implement endpoint health check.
    return new HealthCheckResult().setInstance(endpoint.getHost()).setMessage("Not implemented.")
            .setResult(CheckResult.FAILED).close();
  }

  @Override
  public String toString() {
    return "EndpointHealthCheck{" +
            "endpoint=" + endpoint +
            '}';
  }
}
