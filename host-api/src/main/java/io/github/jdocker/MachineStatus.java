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

/**
 * The possible status of a io.github.jdocker.machine.
 */
public enum MachineStatus {
    /** The io.github.jdocker.machine has a problem. */
    Error,
    /** The io.github.jdocker.machine is not running. */
    Stopped,
    /** The io.github.jdocker.machine is running and active. */
    Running,
    /** There was some unknown error that prevented the evaluation of the status, e.g. docker-io.github.jdocker.machine is
     * not installed.
     */
    Unknown
}
