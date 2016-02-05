package io.github.jdocker.agent;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.jdocker.agent.rest.DockerMachineResource;
import io.github.jdocker.common.ServiceContextManager;
import io.vertx.core.Launcher;

public class DockerAgentMain extends Application<DockerAgentConfiguration> {


    private static DockerAgent agent;

    public static void main(String[] args) throws Exception {
        new DockerAgentMain().run(new String[]{"server"});
    }

    @Override
    public String getName() {
        return "jdocker-agent";
    }

    @Override
    public void initialize(Bootstrap<DockerAgentConfiguration> bootstrap) {
        agent = ServiceContextManager.getServiceContext().getService(DockerAgent.class);
        if(agent==null){
            throw new IllegalStateException("Failed to load agent core.");
        }
    }

//    @Override
//    public void run(HelloWorldConfiguration configuration,
//                    Environment environment) {
//        final HelloWorldResource resource = new HelloWorldResource(
//                configuration.getTemplate(),
//                configuration.getDefaultName()
//        );
//    final TemplateHealthCheck healthCheck =
//            new TemplateHealthCheck(configuration.getTemplate());
//    environment.healthChecks().register("template", healthCheck);
//        environment.jersey().register(resource);
//    }


    @Override
    public void run(DockerAgentConfiguration configuration,
                    Environment environment) {
//        agent.setAgentName(configuration.getAgentName());
        final DockerMachineResource dockerMachine = new DockerMachineResource();
        environment.jersey().register(dockerMachine);
        Launcher.main(new String[]{"run", HeartBeatVerticle.class.getName()});
    }



}