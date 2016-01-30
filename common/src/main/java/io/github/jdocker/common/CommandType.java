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
package io.github.jdocker.common;

/**
 * For these commands logic is required and according listeners/actors must be defined.
 */
public enum CommandType {
    /* IPAM */
    DEFINE_POOL,
    RELEASE_POOL,

    /*Deploy /undeploy */
    DEPLOY_CONTAINER,
    DEPLOY_MACHINE,
    DEPLOY_NETWORK,
    DEPLOY_SWARM,

    UNDEPLOY_SWARM,
    UNDEPLOY_CONTAINER,
    UNDEPLOY_MACHINE,
    UNDEPLOY_NETWORK,

    /* SSH. */
    EXECUTE_COMMAND,

    /*  DNS handling. */
    REGISTER_SERVICE,
    UNREGISTER_SERVICE,
    REGISTER_HOST,
    UNREGISTER_HOST,
    REGISTER_CONTAINER,
    UNREGISTER_CONTAINER,

    /* Security. */
    DEFINE_USER,
    DEFINE_SECURITYPOLICY,

    /* Scaling. */
    SCALE_UP,
    SCALE_DOWN,

    /* Failover. */
    RELOCATE,
    MASTER_SYNCH,
    CONFIG_UPDATE,

    /* Monitoring. */
    FAILURE,
    LATENCY,
    AUTODISCOVERY,
    OVERLOAD,
    UNDERLOAD,
}
