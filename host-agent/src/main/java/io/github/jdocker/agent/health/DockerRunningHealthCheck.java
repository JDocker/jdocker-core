package io.github.jdocker.agent.health;//package dropexample;

import com.codahale.metrics.health.HealthCheck;
import io.github.jdocker.agent.DockerAgent;

public class DockerRunningHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        DockerAgent agent = DockerAgent.getInstance();
        switch(agent.getStatus()){
            case Failed:
                return Result.unhealthy("Unknown agent failure, check logs.");
            case Stopped:
                return Result.unhealthy("Docker not running.");
            case Running:
            default:
                return Result.healthy();
        }
    }
}