package io.github.jdocker.serviceregistry.internal;

import io.github.jdocker.common.Endpoint;
import io.github.jdocker.common.EndpointResolutionPolicy;
import io.github.jdocker.common.ServiceDiscovery;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by atsticks on 07.02.16.
 */
public class TamayaServiceDiscovery implements ServiceDiscovery{

    private static final Logger LOG = Logger.getLogger(TamayaServiceDiscovery.class.getName());

    @Override
    public Collection<Endpoint> getEndpoints(String serviceName, Collection<String> tags) {
        List<Endpoint> endpoints = new ArrayList<>();
        Configuration config = ConfigurationProvider.getConfiguration();
        String serviceRefs = config.get("jd.service."+serviceName);
        for (String endpointRef : serviceRefs.split(",")) {
            for (String host : endpointRef.split(",")) {
                host = host.trim();
                String endpointDef = config.get("jd.endpoint." + host+".uri");
                if (endpointDef != null) {
                    URI endpointURI = null; // protocol:/host_port/domain?tags=a,b,c,d&healthCheck=checkId
                    try {
                        endpointURI = new URI(endpointDef);
                        Endpoint ep = new Endpoint(serviceName, endpointURI, tags);
                        if(ep.matchTags(tags)) {
                            endpoints.add(ep);
                        }
                    } catch (URISyntaxException e) {
                        LOG.log(Level.SEVERE, "Failed to read endpoint from config: " +endpointDef, e);
                    }
                }
            }
        }
        return endpoints;
    }

    @Override
    public Endpoint getEndpoint(String serviceName, Collection<String> tags) {
        return EndpointResolutionPolicy.RANDOM_RESOLUTIONPOLICY.resolve(getEndpoints(serviceName, tags));
    }

    @Override
    public Endpoint registerEndpoint(String serviceName, URI endpoint, Collection<String> tags) {
        Endpoint ep = new Endpoint(serviceName, endpoint, tags);
        registerEndpoint(ep);
        return ep;
    }

    @Override
    public void registerEndpoint(Endpoint endpoint) {
        String serviceRefs = MutableTestPropertySource.getSharedConfig().get("jd.services."+endpoint.getServiceName());
        if(serviceRefs==null){
            serviceRefs = endpoint.getServiceName();
            // create service and endpoint entry.
            MutableTestPropertySource.getSharedConfig().put("jd.service." + endpoint.getServiceName()+".hosts", endpoint.getHost());
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".uri", endpoint.getURI().toString());
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".tags", endpoint.getTags().toString());
        }
        else if(!serviceRefs.contains(endpoint.getHost())){
            // only update endpoint
            serviceRefs = serviceRefs + ","+endpoint.getHost();
            MutableTestPropertySource.getSharedConfig().put("jd.service." + endpoint.getServiceName()+".hosts", serviceRefs);
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".uri", endpoint.getURI().toString());
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".tags", endpoint.getTags().toString());
        }
        else{
            // add service entry and update/add entrypoint
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".uri", endpoint.getURI().toString());
            MutableTestPropertySource.getSharedConfig().put("jd.endpoint." + endpoint.getHost()+".tags", endpoint.getTags().toString());
        }
    }

    @Override
    public Endpoint removeEndpoint(String serviceName, URI endpoint, Collection<String> tags) {
        Endpoint ep = new Endpoint(serviceName, endpoint, tags);
        removeEndpoint(ep);
        return ep;
    }

    @Override
    public void removeEndpoint(Endpoint endpoint) {
        String serviceRefs = MutableTestPropertySource.getSharedConfig().get("jd.services."+endpoint.getServiceName());
        if(serviceRefs==null){
            return;
        }
        else if(!serviceRefs.contains(endpoint.getHost())){
            return;
        }
        else{
            // remove service entry and update/add entrypoint
            serviceRefs = serviceRefs.replace(","+endpoint.getHost()+",", "");
            serviceRefs = serviceRefs.replace(endpoint.getHost()+",", "");
            serviceRefs = serviceRefs.replace(","+endpoint.getHost(), "");
            MutableTestPropertySource.getSharedConfig().put("jd.service." + endpoint.getServiceName()+".hosts", serviceRefs);
            MutableTestPropertySource.getSharedConfig().remove("jd.endpoint." + endpoint.getHost()+".uri");
            MutableTestPropertySource.getSharedConfig().remove("jd.endpoint." + endpoint.getHost()+".tags");
        }
    }
}
