/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.jdocker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by atsticks on 13.01.16.
 */
public class DockerClient {

    /** Location of client config files, default {@code ~/.docker}, maps to param {@code --config=<loc>}. */
    private String configLocation; // default:
    /** Enable debug mode, maps to {@code -D, --debug=false}. */
    private boolean debugEnabled;
    /** Do not contact legacy registries, maps to {@code --disable-legacy-registry=false}. */
    private boolean disableLegacyRegistries;
    /** Daemon socket(s) to connect to, maps to {@code -H, --host=[]}. */
    private List<String> hosts = new ArrayList<>();
    /** Set the docker log level, maps to {@code -l, --log-level=info }. */
    private String logLevel;
    /** Use TLS; implied by --tlsverify, maps to {@code --tls=false}. */
    private boolean useTLS;
    /** Trust certs signed only by this CA, maps to {@code --tlscacert=~/.docker/ca.pem}. */
    private String tlsCSCertificate;
    /** Path to TLS certificate file, maps to {@code --tlscert=~/.docker/cert.pem}. */
    private String tlsCertificate;
    /** Path to TLS key file, maps to {@code --tlskey=~/.docker/key.pem }. */
    private String tlsKey;
    /** Use TLS and verify the remote, maps to {@code --tlsverify=false}. */
    private boolean tlsVerify;

    // docker attach    Attach to a running container
    // events    Get real time events from the server

    /**
     * Evaluate the running docker version, by calling {@code docker -v}.
     * @return the docker version.
     */
    public String getDockerVersion(){
        return Executor.execute("docker -v");
    }

    public String getInfo(){
        return Executor.execute("docker info");
    }

    public void buildImage(File file){
        // docker build     Build an image from a Dockerfile
        throw new UnsupportedOperationException("Not implemented");
    }


    public void commitChangesToNewImage(String newImageName){
        // docker commit    Create a new image from a container's changes
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker cp        Copy files/folders from a container to a HOSTDIR or to STDOUT
    public String catData(String data){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker create
    public DockerContainer createContainer(String name){
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getContainerDiff(String containerName){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker exec      Run a command in a running container
    public String execCommand(String command, String container){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker export    Export a container's filesystem as a tar archive
    public File exportContainer(String container){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker history   Show the history of an image
    public String getContainerHistory(String container){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker images
    public List<DockerImage> getImages(){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker ps        List containers
    public List<DockerContainer> getContainers(){
        throw new UnsupportedOperationException("Not implemented");
    }

    // docker import    Import the contents from a tarball to create a filesystem image
    public DockerImage importImage(File imageRoot){
        throw new UnsupportedOperationException("Not implemented");
    }

    // Return low-level information on a container or image
    public String getContainerInfo(String container){
        return Executor.execute("docker info " + container);
    }

//    kill      Kill a running container
//    load      Load an image from a tar archive or STDIN
//    login     Register or log in to a Docker registry
//    logout    Log out from a Docker registry
//    logs      Fetch the logs of a container
//    pause     Pause all processes within a container
//    port      List port mappings or a specific mapping for the CONTAINER
//
//    pull      Pull an image or a repository from a registry
//    push      Push an image or a repository to a registry
//    rename    Rename a container
//    restart   Restart a running container
//    rm        Remove one or more containers
//    rmi       Remove one or more images
//    run       Run a command in a new container
//    save      Save an image(s) to a tar archive
//    search    Search the Docker Hub for images
//    start     Start one or more stopped containers
//    stats     Display a live stream of container(s) resource usage statistics
//    stop      Stop a running container
//    tag       Tag an image into a repository
//    top       Display the running processes of a container
//    unpause   Unpause all processes within a container
//    version   Show the Docker version information
//    wait      Block until a container stops, then print its exit code

}
