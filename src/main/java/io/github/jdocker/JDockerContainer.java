package io.github.jdocker;

import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ContainerState;

/**
 * Structure that allows to identify a container globally.
 */
public interface JDockerContainer {

    ContainerInfo getInstance();

    String getHostName();

    default JDockerMachine getHost(){
        return Machines.getDocker(getHostName());
    }

    default String getId(){
        return getInstance().id();
    }

    default String getName(){
        return getInstance().name();
    }

    default String getImage(){
        return getInstance().image();
    }

    default String getDriver(){
        return getInstance().driver();
    }

    default ContainerState getState(){
        return getInstance().state();
    }

    default String getIPAddress(){
        return getInstance().networkSettings().ipAddress();
    }

    default String getMacAddress(){
        return getInstance().networkSettings().macAddress();
    }

    default String getGateway(){
        return getInstance().networkSettings().gateway();
    }

    default String getPortsInfo(){
        return String.valueOf(getInstance().networkSettings().ports());
    }


}
