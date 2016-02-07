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
import java.net.URISyntaxException;
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
    private java.net.URI uri;


    /**
     * Creates a new endpoint without specific location.
     * @param serviceName the endpoint/service name.
     * @param tags optional tags, e.g. {@code tags=stage:test,zone:rz,zone:ch}.
     */
    public Endpoint(final String serviceName, Collection<String> tags) {
        this.serviceName = Objects.requireNonNull(serviceName);
        if(tags!=null){
            this.tags.addAll(tags);
        }
    }

    /**
     * Creates a new endpoint.
     * @param serviceName the endpoint/service name.
     * @param protocol the protocol
     * @param port the port
     * @param host the host
     * @param tags optional tags, e.g. {@code tags=stage:test,zone:rz,zone:ch}.
     */
    public Endpoint(final String serviceName, final String protocol, final int port,
                    final String host, String path, String query, Collection<String> tags) throws URISyntaxException {
        this.serviceName = Objects.requireNonNull(serviceName);
        this.protocol = Objects.requireNonNull(protocol);
        this.port = port;
        this.host = Objects.requireNonNull(host);
        this.tags.addAll(tags);
        this.uri = new URI(protocol, null, host, port, path, query, null);
    }

    /**
     * Creates a new endpoint.
     * @param serviceName the endpoint/service name.
     * @param endpointURI the URI including tags modelled by an optional {@code tags} parameter containing a
     *                    comma separated list of tags, e.g. {@code tags=stage:test,zone:rz,zone:ch}.
     */
    public Endpoint(String serviceName, URI endpointURI, Collection<String> tags) {
        String query = endpointURI.getQuery();
        this.serviceName = Objects.requireNonNull(serviceName);
        this.protocol = Objects.requireNonNull(endpointURI.getScheme());
        this.port = endpointURI.getPort();
        this.domain = endpointURI.getPath();
        this.host = Objects.requireNonNull(endpointURI.getHost());
        if(tags!=null) {
            this.tags.addAll(tags);
        }
        this.uri = endpointURI;
    }

    /**
     * Checks if any of the given tags is present in the current tag set.
     * @param tags the tags to check.
     * @return true, if any tag is present.
     */
    public boolean matchTags(Collection<String> tags){
        for(String tag:tags){
            if(!tags.contains(tag)){
                return false;
            }
        }
        return true;
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

    public URI getURI() {
        return uri;
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
