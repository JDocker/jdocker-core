package io.github.jdocker.deployment;

import io.github.jdocker.DockerNode;

import java.util.Collection;

/**
 * Class that defines which docker nodes can be used for this deployment.
 */
public interface DockerNodeElector {

    /**
     * Evaluates the possible deployment targets. This does a selection from all known nodes in the system that
     * match the deployment's configuration. It is the responsibility of the {@link DockerNodeSelector} to
     * effectively define the effective deployment targets called for deployment.
     * @param deployment the deployment, not null
     * @return the collection of eligible nodes, never null.
     */
    Collection<DockerNode> evaluateTargetNodes(Deployment deployment);

}
