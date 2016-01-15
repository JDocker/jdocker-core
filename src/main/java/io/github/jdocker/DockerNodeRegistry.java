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

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registry managing the docker registries known to this master orchestration node.
 */
public class DockerNodeRegistry {

    private static final Logger LOG = Logger.getLogger(DockerNodeRegistry.class.getName());
    private static final Map<String, DockerNode> DOCKERS = new ConcurrentHashMap<>();

    static{
        // read master config from Tamaya
        Configuration config = ConfigurationProvider.getConfiguration();
        Map<String,String> props = config.getProperties();
        String masterDockers = props.get("jdocker.docker.masters");
        if(masterDockers!=null){
            for(String masterKey:masterDockers.split(",")){
                Map<String,String> labels = readLabelForDockerContainer(masterKey, config);
                DockerClient client = initDockerClient(masterKey, config);
                DOCKERS.put(masterKey, new DockerNode(masterKey, client, labels));
                LOG.info("Registered docker instance: " + masterKey);
            }
        }
    }

    private static DockerClient initDockerClient(String dockerName, Configuration config) {
        DefaultDockerClient.Builder builder = DefaultDockerClient.builder();
        String apiVersion = config.get("jdocker.docker."+dockerName+".apiVersion");
        if(apiVersion!=null){
            builder.apiVersion(apiVersion);
        }
    }

    private static Map<String, String> readLabelForDockerContainer(String dockerName, Configuration config) {
        Map<String,String> labels = new HashMap<>();
        String labelsProp = config.get("jdocker.docker."+dockerName+".labels");
        if(labelsProp!=null){
            StringTokenizer tokenizer = new StringTokenizer(labelsProp, ",\n\r", false);
            while(tokenizer.hasMoreTokens()){
                String token = tokenizer.nextToken();
                if(token.trim().isEmpty()){
                    continue;
                }
                int index = token.indexOf('=');
                if(index<0){
                    LOG.warning("Found non interpretable label '" + token + "' for docker container '"
                            +dockerName + "', ignoring it.");
                }
                String key = token.substring(0,index).trim();
                String value = token.substring(index+1);
                labels.put(key, value);
            }
        }
        return labels;
    }

    private DockerNodeRegistry(){}

    public static DockerNode addDocker(String name, DockerClient client, Map<String,String> labels){
        DockerNode dockerNode = new DockerNode(name, client, labels);
        DockerNodeRegistry.DOCKERS.put(name, dockerNode);
        return dockerNode;
    }

    public static DockerNode getDocker(String name){
        return DockerNodeRegistry.DOCKERS.remove(name);
    }

    public static Collection<DockerNode> getDockers(){
        return DockerNodeRegistry.DOCKERS.values();
    }

    public static Set<String> getDockerNames(){
        return DockerNodeRegistry.DOCKERS.keySet();
    }

    public static DockerNode removeDocker(String name){
        return DockerNodeRegistry.DOCKERS.remove(name);
    }

}
