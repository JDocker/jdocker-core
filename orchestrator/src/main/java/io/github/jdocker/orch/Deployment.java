package io.github.jdocker.orch;

import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.network.AddressPool;
import io.github.jdocker.network.SecurityProfile;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by atsticks on 04.02.16.
 */
public class Deployment {
    private String deploymentId = UUID.randomUUID().toString();
    private ContainerInfo containerInfo;
    private String network;
    private AddressPool addressPool;
    private String address;
    private SecurityProfile securityProfile;

    public static DeploymentBuilder builder(){
        return new DeploymentBuilder();
    }

    Deployment(DeploymentBuilder builder){
        if(builder.deploymentId!=null){
            this.deploymentId = builder.deploymentId;
        }
        this.containerInfo = Objects.requireNonNull(builder.containerInfo);
        this.network = builder.network;
        this.address = builder.address;
        this.addressPool = builder.addressPool;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public ContainerInfo getContainerInfo() {
        return containerInfo;
    }

    public String getNetwork() {
        return network;
    }

    public AddressPool getAddressPool() {
        return addressPool;
    }

    public String getAddress() {
        return address;
    }

    public SecurityProfile getSecurityProfile() {
        return securityProfile;
    }


}
