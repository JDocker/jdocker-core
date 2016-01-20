package io.github.jdocker.swarm;

import io.github.jdocker.common.Executor;

/**
 * Created by atsticks on 17.01.16.
 */
public final class Swarms {

    private Swarms(){}

    public static String createDiscoveryToken(){
        return Executor.execute("curl -s -XPOST https://discovery-stage.hub.docker.com/v1/clusters");
    }



}
