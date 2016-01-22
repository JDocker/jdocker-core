package io.github.jdocker.events;

import io.github.jdocker.UnpooledMachine;

/**
 * Created by atsticks on 22.01.16.
 */
public class DockerInstallRequest {

    private UnpooledMachine machine;

    public DockerInstallRequest(UnpooledMachine machine){
        this.machine = machine;
    }

    public UnpooledMachine getUnpooledMachine(){
        return machine;
    }
}
