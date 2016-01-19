package io.github.jdocker;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import io.github.jdocker.machine.Machine;
import io.github.jdocker.machine.MachineConfig;
import io.github.jdocker.machine.Machines;

import java.util.ArrayList;
import java.util.List;

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
            Machine machine = Machines.createMachine(machineConfig);

            // Configure port bindings...
            List<PortBinding> containerPorts = new ArrayList<PortBinding>();
            containerPorts.add(PortBinding.of("0.0.0.0", "443"));
            portBindings.put("443", randomPort);

            // Add container as member to a network
            final HostConfig hostConfig = HostConfig.builder() // --net
                    .networkMode("container:net1").build();

            // Create container with exposed ports
            final ContainerConfig containerConfig = ContainerConfig.builder()
            ContainerConfig container = ContainerConfig.builder()
                    .hostname()
            try {
                Machines.getMachine()registerMachineAsDockerNode("test=true", "auto-remove=true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
