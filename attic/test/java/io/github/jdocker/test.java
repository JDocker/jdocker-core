package io.github.jdocker;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;

/**
 * Created by atsticks on 15.01.16.
 */
public class test {

    public static void main(String... args) {
        for(int i=0;i<10;i++) {
            MachineConfig machineConfig = MachineConfig.builder("test"+i)
                    .addLabel("test")
                    .setDriver("virtualbox")
                    .build();
            JDockerHost machine = Machines.createMachine(machineConfig);

//            // Configure port bindings...
//            List<PortBinding> containerPorts = new ArrayList<PortBinding>();
//            containerPorts.add(PortBinding.of("0.0.0.0", "443"));
//            portBindings.put("443", randomPort);

            // Add container as member to a network
            final HostConfig hostConfig = HostConfig.builder() // --net
                    .networkMode("container:test-dev")
                    .build();
            // Create container with exposed ports
            ContainerConfig.Builder cfgBuilder = ContainerConfig.builder()
                    .hostConfig(hostConfig).image("busybox");
                cfgBuilder.labels()
                    .put("ttl", String.valueOf(System.currentTimeMillis()+3600000L));
            Deployment deployment = new DeploymentBuilder().addRequest(
                    cfgBuilder.build()).build();
            try {
                Deployer.deploy(deployment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(JDockerHost machine:Machines.getDockerHosts()) {
            if(machine.getSimpleName().startsWith("test")){
                machine.stop();
                machine.remove();
            }
        }

    }
}
