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

import java.io.Serializable;
import java.net.URI;
import java.util.*;

/**
 * Class that implements the basic functionality of an endpoint type.
 */
public class Endpoint implements Serializable{

    private final String serviceName;
    private String protocol;
    private int port;
    private String domain;
    /**
     * The hostname on which we will advertise this service in service discovery
     */
    private String host;
    private Set<String> tags = new HashSet<>();
    /** Flag if the endpoint instance is closed/read-only. */
    private boolean closed;


    public Endpoint(final String serviceName) {
        this.serviceName = Objects.requireNonNull(serviceName);
    }

    public Endpoint(final String serviceName, final String protocol, final int port,
                    final String host) {
        this.serviceName = Objects.requireNonNull(serviceName);
        this.protocol = Objects.requireNonNull(protocol);
        this.port = port;
        this.host = Objects.requireNonNull(host);
        this.tags.addAll(tags);
    }

    public Endpoint(String endpointName, URI endpointURI) {
        String query = endpointURI.getQuery();
        Set<String> tags = new HashSet<>();
        if (query != null) {
            StringTokenizer tokenizer = new StringTokenizer(query, "&", false);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                int index = token.indexOf('=');
                if (index > 0) {
                    String key = token.substring(0, index);
                    String value = token.substring(index + 1);
                    if (token.startsWith("labels")) {
                        tags.addAll(Arrays.asList(value.split(",")));
                    }
                }
            }
        }
        this.serviceName = Objects.requireNonNull(endpointName);
        this.protocol = Objects.requireNonNull(endpointURI.getScheme());
        this.port = endpointURI.getPort();
        this.domain = endpointURI.getPath();
        this.host = Objects.requireNonNull(endpointURI.getHost());
        this.tags.addAll(tags);
    }

    public String getHost() {
        return host;
    }

    public String getDomain() {
        return domain;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Endpoint setProtocol(String protocol) {
        ensureClosed();
        this.protocol = protocol;
        return this;
    }

    public Endpoint setPort(int port) {
        ensureClosed();
        this.port = port;
        return this;
    }

    public Endpoint setDomain(String domain) {
        ensureClosed();
        this.domain = domain;
        return this;
    }

    public Endpoint setHost(String host) {
        ensureClosed();
        this.host = host;
        return this;
    }

    public Endpoint addTags(String... tags) {
        ensureClosed();
        for(String tag:tags){
            this.tags.add(tag);
        }
        return this;
    }

    public Endpoint setTags(Set<String> tags) {
        ensureClosed();
        this.tags.clear();
        this.tags.addAll(tags);
        return this;
    }

    protected final void ensureClosed() {
        if(closed){
            throw new IllegalStateException("Endpoint is closd/readonly.");
        }
    }

    public Endpoint close(){
        closed=true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;

        Endpoint endpoint = (Endpoint) o;

        return serviceName.equals(endpoint.serviceName);

    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "serviceName='" + serviceName + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                ", domain='" + domain + '\'' +
                ", host='" + host + '\'' +
                ", tags=" + tags +
                '}';
    }
}
