package io.github.jdocker.events;

import io.github.jdocker.MachineConfig;

import java.util.Objects;

/**
 * Created by atsticks on 22.01.16.
 */
public final class MachineRemovalRequest {

    private String machineId;

    public MachineRemovalRequest(String machineId){
        this.machineId = Objects.requireNonNull(machineId);
    }

    public String getMachineId(){
        return machineId;
    }
}
