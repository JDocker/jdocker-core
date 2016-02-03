package io.github.jdocker.agent.rest;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.jdocker.agent.DockerAgentConfiguration;
import io.github.jdocker.agent.health.AgentNameHealthCheck;

public class DockerAgentMain extends Application<DockerAgentConfiguration> {

    public static void main(String[] args) throws Exception {
        new DockerAgentMain().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<DockerAgentConfiguration> bootstrap) {
        // nothing to do yet
    }

//    @Override
//    public void run(HelloWorldConfiguration configuration,
//                    Environment environment) {
//        final HelloWorldResource resource = new HelloWorldResource(
//                configuration.getTemplate(),
//                configuration.getDefaultName()
//        );

//    }


    @Override
    public void run(DockerAgentConfiguration configuration,
                    Environment environment) {
        final DockerMachineResource hello = new DockerMachineResource();
        environment.jersey().register(hello);

        final HealthCheck healthCheck =
                new AgentNameHealthCheck(configuration.getAgentName());
        environment.healthChecks().register("agent-name", healthCheck);
    }



}