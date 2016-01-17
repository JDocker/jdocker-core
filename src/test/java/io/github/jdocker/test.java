package io.github.jdocker;

import io.github.jdocker.machine.MachineConfig;

/**
 * Created by atsticks on 15.01.16.
 */
public class test {

    public static void main(String... args) {
        for(int i=0;i<10;i++) {
            MachineConfig machine = MachineConfig.builder("test"+i)
                    .addLabel("test")
                    .setDriver("virtualbox")
                    .build();
            try {
                machine.registerMachineAsDockerNode("test=true", "auto-remove=true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
