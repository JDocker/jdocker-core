package io.github.jdocker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerInfo;
import io.github.jdocker.internal.DefaultContainer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by atsticks on 19.01.16.
 */
public final class Containers {

    private static final Logger LOG = Logger.getLogger(Containers.class.getName());

    private Containers(){}

    public static Collection<JDockerContainer> getContainers(String containerName)   {
        List<JDockerContainer> result = new ArrayList<>();
        for(JDockerMachine docker: Machines.getDockers()) {
            DockerClient client = docker.createDockerClient();
            List<com.spotify.docker.client.messages.Container> containers = null;
            try {
                containers = client.listContainers(
                        DockerClient.ListContainersParam.create("name", containerName));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            for(com.spotify.docker.client.messages.Container info: containers) {
                try {
                    ContainerInfo cInfo = client.inspectContainer(info.id());
                    result.add(new DefaultContainer(cInfo, docker.getName()));
                }
                catch(Exception e){
                    LOG.log(Level.WARNING, "Failed to read container state for " + containerName + " from Docker-Node "
                            + docker.getName(), e);
                }
            }
        }
        return result;
    }

    public static Collection<JDockerContainer> getExitedContainers()   {
        List<JDockerContainer> result = new ArrayList<>();
        for(JDockerMachine docker: Machines.getDockers()) {
            DockerClient client = docker.createDockerClient();
            List<com.spotify.docker.client.messages.Container> containers = null;
            try {
                containers = client.listContainers(
                        DockerClient.ListContainersParam.exitedContainers());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            for(com.spotify.docker.client.messages.Container info: containers) {
                try {
                    ContainerInfo cInfo = client.inspectContainer(info.id());
                    result.add(new DefaultContainer(cInfo, docker.getName()));
                }
                catch(Exception e){
                    LOG.log(Level.WARNING, "Failed to read container state for " + info.id() + " (exited) from Docker-Node "
                            + docker.getName(), e);
                }
            }
        }
        return result;
    }

    public static Collection<JDockerContainer> getContainersWithLabels(String... labels)   {
        List<JDockerContainer> result = new ArrayList<>();
        for(JDockerMachine docker: Machines.getDockers()) {
            DockerClient client = docker.createDockerClient();
            List<com.spotify.docker.client.messages.Container> containers = null;
            try {
                containers = client.listContainers(createLabelParams(labels));
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Failed to read containers with labels " + Arrays.toString(labels), e);
                continue;
            }
            for(com.spotify.docker.client.messages.Container info: containers) {
                try {
                    ContainerInfo cInfo = client.inspectContainer(info.id());
                    result.add(new DefaultContainer(cInfo, docker.getName()));
                }
                catch(Exception e){
                    LOG.log(Level.WARNING, "Failed to read container state for " + info.id() + " (exited) from Docker-Node "
                            + docker.getName(), e);
                }
            }
        }
        return result;
    }

    private static DockerClient.ListContainersParam[] createLabelParams(String[] labels) {
        DockerClient.ListContainersParam[] result = new DockerClient.ListContainersParam[labels.length];
        for(int i=0;i<labels.length;i++){
            int index = labels[i].indexOf('=');
            if(index<=0){
                result[i] = DockerClient.ListContainersParam.withLabel(labels[i]);
            }
            else{
                result[i] = DockerClient.ListContainersParam.withLabel(labels[i].substring(0,index), labels[i].substring(index+1));
            }
        }
        return result;
    }

    public static void removeContainer(JDockerContainer container) {
        try {
            Machines.getDocker(container.getHostName()).createDockerClient().removeContainer(container.getInstance().id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopContainer(JDockerContainer container, int secondsBeforeKilling) {
        try {
            Machines.getDocker(container.getHostName()).createDockerClient().stopContainer(
                    container.getInstance().id(), secondsBeforeKilling);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startContainer(JDockerContainer container) {
        try {
            Machines.getDocker(container.getHostName()).createDockerClient().startContainer(
                    container.getInstance().id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
