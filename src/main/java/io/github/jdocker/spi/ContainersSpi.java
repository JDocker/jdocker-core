package io.github.jdocker.spi;

import io.github.jdocker.JDockerContainer;

import java.util.Collection;

/**
 * Created by atsticks on 19.01.16.
 */
public interface ContainersSpi {

    Collection<JDockerContainer> getContainers(String containerName);

    Collection<JDockerContainer> getExitedContainers();

    Collection<JDockerContainer> getContainersWithLabels(String... labels);

    void removeContainer(JDockerContainer container);

    void stopContainer(JDockerContainer container, int secondsBeforeKilling);

    void startContainer(JDockerContainer container);

}
