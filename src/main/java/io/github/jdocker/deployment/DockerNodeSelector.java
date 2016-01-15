package io.github.jdocker.deployment;

import io.github.jdocker.DockerNode;

import java.util.Collection;
import java.util.Map;

/**
 * Instances implementing this interface are responsible for defining the effective nodes that will be called for
 * deployment.
 */
public interface DockerNodeSelector {

    /**
     * Select the effective target nodes from the eligible nodes.
     * @param electedNodes the node set from where to choose the best matching nodes, e.g. based on existing usage and
     *                     load, or other runtime characteristics.
     * @param deployment the target deployment, not null
     * @return the set of nodes to be called for deployment, never null. If the number of nodes does not match the
     * requested {@link Deployment#getTargetScale()} a warning must be logged.
     */
    Collection<DockerNode> selectTargetNodes(Collection<DockerNode> electedNodes, Deployment deployment);
    
}
