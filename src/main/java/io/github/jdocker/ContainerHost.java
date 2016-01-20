package io.github.jdocker;

import com.spotify.docker.client.messages.ContainerInfo;

/**
 * Structure that allows to identify a container globally.
 */
public interface ContainerHost {

    ContainerInfo getInstance();
    String dockerNode();

}
