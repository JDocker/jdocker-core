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
package io.github.jdocker.networking.weave;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     "IPAM": {
 * "Paxos": null,
 * "Range": "10.32.0.0-10.47.255.255",
 * "RangeNumIPs": 1048576,
 * "DefaultSubnet": "10.32.0.0/12",
 * "Entries": null,
 * "PendingClaims": null,
 * "PendingAllocates": null
 * },
 * </pre>
 */
public class IPAMConfig {

    private String paxos;
    private String range;
    private int rangeNumIps;
    private String defaultSubnet;
    private List<String> pendingClaims = new ArrayList<>();
    private List<String> pendingAllocates = new ArrayList<>();

    /**
     * Gets paxos.
     *
     * @return the paxos
     */
    public String getPaxos() {
        return paxos;
    }

    /**
     * Gets range.
     *
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * Gets range num ips.
     *
     * @return the range num ips
     */
    public int getRangeNumIps() {
        return rangeNumIps;
    }

    /**
     * Gets default subnet.
     *
     * @return the default subnet
     */
    public String getDefaultSubnet() {
        return defaultSubnet;
    }

    /**
     * Gets pending claims.
     *
     * @return the pending claims
     */
    public List<String> getPendingClaims() {
        return pendingClaims;
    }

    /**
     * Gets pending allocates.
     *
     * @return the pending allocates
     */
    public List<String> getPendingAllocates() {
        return pendingAllocates;
    }
}
