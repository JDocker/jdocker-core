package io.github.jdocker.orch;

import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.network.AddressPool;
import io.github.jdocker.network.SecurityProfile;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by atsticks on 05.02.16.
 */
public class DeploymentBuilder {

    String deploymentId = UUID.randomUUID().toString();
    ContainerInfo containerInfo;
    String network;
    AddressPool addressPool;
    String address;
    SecurityProfile securityProfile;

    public DeploymentBuilder setAddress(String address) {
        this.address = Objects.requireNonNull(address);
        return this;
    }

    public DeploymentBuilder setAddressPool(AddressPool addressPool) {
        this.addressPool = Objects.requireNonNull(addressPool);
        return this;
    }

    public DeploymentBuilder setContainerInfo(ContainerInfo containerInfo) {
        this.containerInfo = Objects.requireNonNull(containerInfo);
        return this;
    }

    public DeploymentBuilder setNetwork(String network) {
        this.network = Objects.requireNonNull(network);
        return this;
    }

    public DeploymentBuilder setSecurityProfile(SecurityProfile securityProfile) {
        this.securityProfile = Objects.requireNonNull(securityProfile);
        return this;
    }

    public DeploymentBuilder setDeploymentId(String deploymentId) {
        this.deploymentId = Objects.requireNonNull(deploymentId);
        return this;
    }

    public Deployment build(){
        return new Deployment(this);
    }

    @Override
    public String toString() {
        return "DeploymentBuilder{" +
                "address='" + address + '\'' +
                ", deploymentId='" + deploymentId + '\'' +
                ", containerInfo=" + containerInfo +
                ", network='" + network + '\'' +
                ", addressPool=" + addressPool +
                ", securityProfile=" + securityProfile +
                '}';
    }
}
