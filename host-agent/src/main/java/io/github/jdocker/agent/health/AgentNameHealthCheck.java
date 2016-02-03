package io.github.jdocker.agent.health;//package dropexample;

import com.codahale.metrics.health.HealthCheck;

public class AgentNameHealthCheck extends HealthCheck {
    private final String agentName;

    public AgentNameHealthCheck(String agentName) {
        this.agentName = agentName;
    }

    @Override
    protected Result check() throws Exception {
        if(agentName==null || agentName.isEmpty()){
            return Result.unhealthy("Agent name dis missing or empty.");
        }
        // TODO Check if agent name is unique!
        return Result.healthy();
    }
}