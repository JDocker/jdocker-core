package io.github.jdocker;

import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.ContainerConfig;

/**
 * Created by atsticks on 15.01.16.
 */
public class test {

    public void main() throws DockerCertificateException {
        com.spotify.docker.client.DockerClient docker = DefaultDockerClient.fromEnv()
                .connectionPoolSize(100)
                .build();

    }
}
