package io.github.jdocker.deployment;

import com.spotify.docker.client.messages.ContainerCreation;
import io.github.jdocker.DockerNode;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the deployment of new container.
 */
public final class Deployer {

    private static final Logger LOG = Logger.getLogger(Deployer.class.getName());

    public static Collection<DockerNode> getEligibleNodes(Deployment deployment) {
        return ServiceContextManager.getServiceContext().getService(DockerNodeElector.class).evaluateTargetNodes(
                deployment);
    }

    public List<ContainerCreation> deploy(Deployment deployment) {
        Collection<DockerNode> nodes = getEligibleNodes(deployment);
        nodes = ServiceContextManager.getServiceContext().getService(DockerNodeSelector.class).selectTargetNodes(
                nodes, deployment);
        List<ContainerCreation> nodesDeployed = new ArrayList<>();
        for (DockerNode node : nodes) {
            nodesDeployed.add(deployDirect(node, deployment));
        }
        return nodesDeployed;
    }

    public ContainerCreation  deployDirect(DockerNode node, Deployment deployment) {
        try {
            ContainerCreation result = node.getClient().createContainer(deployment.getContainerConfig());
            deployment.addContainer(node.getClient().inspectContainer(result.id()));
            return result;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Container deployment failed for " + deployment + " on node " + node.getName());
            return null;
        }
    }


}