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
 *     "DNS": {
 * "Domain": "weave.local.",
 * "Upstream": [
 * "192.168.1.1",
 * "62.2.17.61"
 * ],
 * "Address": "172.17.0.1:53",
 * "TTL": 1,
 * "Entries": null
 * }
 * </pre>
 */
public class DNSConfig {
    private String domain;
    private List<String> upstreamNodes = new ArrayList<>();
    private String address;
    private boolean ttl;

    /**
     * Gets domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Gets upstream nodes.
     *
     * @return the upstream nodes
     */
    public List<String> getUpstreamNodes() {
        return upstreamNodes;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Is ttl boolean.
     *
     * @return the boolean
     */
    public boolean isTtl() {
        return ttl;
    }
}
