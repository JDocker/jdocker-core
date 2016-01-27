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
package io.github.jdocker;

import io.github.jdocker.common.HealthCheckResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by atsticks on 27.01.16.
 */
public class DockerAgentStatus {

    public enum Status{
        RUNNING,
        RUNNING_WITH_WARNINGS,
        RUNNING_ERRORS,
        NOT_RUNNING
    }

    private String id;
    private String dockerName;
    private Status agentStatus = Status.RUNNING;
    private Map<Level, List<HealthCheckResult>> healtchChecks = new HashMap<>();

    public String getId() {
        return id;
    }

    public String getDockerName() {
        return dockerName;
    }

    public Status getAgentStatus() {
        return agentStatus;
    }

    public Map<Level, List<HealthCheckResult>> getHealtchChecks() {
        return healtchChecks;
    }
}
