package io.github.jdocker.events;

import io.github.jdocker.MachineConfig;

import java.util.Objects;

/**
 * Created by atsticks on 22.01.16.
 */
public final class MachineRequest {

    private MachineConfig machineConfig;

    public MachineRequest(MachineConfig config){
        this.machineConfig = Objects.requireNonNull(machineConfig);
    }

    public MachineConfig getMachineConfig(){
        return machineConfig;
    }
}
