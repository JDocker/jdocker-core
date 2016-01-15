package io.github.jdocker.deployment;

import io.github.jdocker.machine.Machine;

import java.util.List;

/**
 * Created by atsticks on 14.01.16.
 */
public interface MachineSelectionPolicy {

    List<Machine> selectMachines(List<Machine> machines, DeploymentRequest request);
}
