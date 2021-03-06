package io.github.jdocker.serviceregistry.internal;

import io.github.jdocker.common.Endpoint;
import org.junit.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by atsticks on 08.02.16.
 */
public class TamayaServiceDiscoveryTest {

    TamayaServiceDiscovery discovery = new TamayaServiceDiscovery();

    @Test
    public void testGetEndpoints() throws Exception {
        discovery.getEndpoints("testGetEndpoint");
    }

    @Test
    public void testGetEndpoint() throws Exception {
        discovery.registerEndpoint("testGetEndpoint", new URI("http://localhost.8080/myService"));
        discovery.getEndpoint("testGetEndpoint");
    }

    @Test
    public void testRegisterEndpoint() throws Exception {
        discovery.registerEndpoint("test", new URI("http://localhost.8080/myService"));
    }

    @Test
    public void testRegisterEndpoint1() throws Exception {
        discovery.registerEndpoint("test", new URI("http://localhost.8080/myService"));
    }

    @Test
    public void testRemoveEndpoint() throws Exception {
        discovery.registerEndpoint("testRemoveEndpoint", new URI("http://localhost.8080/myService"));
        discovery.removeEndpoint("testRemoveEndpoint");
    }

    @Test
    public void testRemoveEndpoint1() throws Exception {
        Endpoint ep = new Endpoint("testRemoveEndpoint", new URI("http://localhost.8080/myService"));
        discovery.registerEndpoint("testRemoveEndpoint", new URI("http://localhost.8080/myService"));
        discovery.removeEndpoint(ep);
    }
}