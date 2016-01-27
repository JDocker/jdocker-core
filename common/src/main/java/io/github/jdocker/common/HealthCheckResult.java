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

import io.vertx.core.json.JsonObject;

/**
 * Class encapsulating  a health check.
 */
public class HealthCheckResult {

    private long timestamp = System.currentTimeMillis();
    private String instance;
    private String checkId;
    private String shortDesc;
    private CheckResult result;
    private String message;
    private boolean closed;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public String getInstance() {
        return instance;
    }

    /**
     * Sets instance.
     *
     * @param instance the instance
     * @return the instance
     */
    public HealthCheckResult setInstance(String instance) {
        ensureNotClosed();
        this.instance = instance;
        return this;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     * @return the timestamp
     */
    public HealthCheckResult setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets check id.
     *
     * @return the check id
     */
    public String getCheckId() {
        return checkId;
    }

    /**
     * Gets short desc.
     *
     * @return the short desc
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public CheckResult getResult() {
        return result;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets check id.
     *
     * @param checkId the check id
     * @return the check id
     */
    public HealthCheckResult setCheckId(String checkId) {
        ensureNotClosed();
        this.checkId = checkId;
        return this;
    }

    /**
     * Sets short desc.
     *
     * @param shortDesc the short desc
     * @return the short desc
     */
    public HealthCheckResult setShortDesc(String shortDesc) {
        ensureNotClosed();
        this.shortDesc = shortDesc;
        return this;
    }

    /**
     * Sets result.
     *
     * @param result the result
     * @return the result
     */
    public HealthCheckResult setResult(CheckResult result) {
        ensureNotClosed();
        this.result = result;
        return this;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public HealthCheckResult setMessage(String message) {
        ensureNotClosed();
        this.message = message;
        return this;
    }

    /**
     * Close health check result.
     *
     * @return the health check result
     */
    public HealthCheckResult close(){
        this.closed = true;
        return this;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed(){
        return closed;
    }

    private void ensureNotClosed(){
        if(closed){
            throw new IllegalStateException("HealthCheckResult is closed and immutable.");
        }
    }

    public JsonObject toJson(){
        return new JsonObject().put("TODO", true);
    }

    @Override
    public String toString() {
        return "HealthCheckResult{" +
                "timestamp=" + timestamp +
                ", instance='" + instance + '\'' +
                ", checkId='" + checkId + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", result=" + result +
                ", message='" + message + '\'' +
                ", closed=" + closed +
                '}';
    }
}
