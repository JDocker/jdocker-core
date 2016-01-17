package io.github.jdocker.deployment;

import io.github.jdocker.machine.MachineConfig;

import java.util.List;

/**
 * Created by atsticks on 14.01.16.
 */
public interface MachineSelectionPolicy {

    List<MachineConfig> selectMachines(List<MachineConfig> machines, Deployment request);
}
