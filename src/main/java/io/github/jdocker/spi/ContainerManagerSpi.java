package io.github.jdocker.spi;

import io.github.jdocker.ContainerHost;

import java.util.Collection;

/**
 * Created by atsticks on 19.01.16.
 */
public interface ContainerManagerSpi {

    Collection<ContainerHost> getContainers(String containerName);

    Collection<ContainerHost> getExitedContainers();

    Collection<ContainerHost> getContainersWithLabels(String... labels);

    void removeContainer(ContainerHost container);

    void stopContainer(ContainerHost container, int secondsBeforeKilling);

    void startContainer(ContainerHost container);

}
