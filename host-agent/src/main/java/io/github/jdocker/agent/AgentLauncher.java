package io.github.jdocker.agent;

import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;

/**
 * Created by atsticks on 24.01.16.
 */
public class AgentLauncher extends Launcher{

    public static void main(String... args){
        Launcher.main(new String[]{"run", DockerAgentVerticle.class.getName()});
    }

    public void beforeStartingVertx(VertxOptions options) {
        // read machine IP
        // check for installation of docker, docker-machine and calico
    }

    @Override
    public String getMainVerticle() {
        return DockerAgentVerticle.class.getName();
    }
}
