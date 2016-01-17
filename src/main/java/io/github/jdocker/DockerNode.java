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

import com.spotify.docker.client.DockerClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Java class representing a docker node that is a valid deployment target.
 */
public class DockerNode {

    private String name;
    private DockerClient client;
    private Map<String,String> labels = new HashMap<>();


    public DockerNode(String name, DockerClient client, String... labels){
        this.name = Objects.requireNonNull(name);
        this.client = Objects.requireNonNull(client);
        for(String lbl:labels){
            int index = lbl.indexOf('=');
            if(index>0){
                this.labels.put(lbl.substring(0,index).trim(),
                        lbl.substring(index+1));
            }
            else{
                this.labels.put(lbl, "");
            }
        }
    }

    public String getName(){
        return name;
    }

    public String getSimpleName(){
        int index = name.lastIndexOf('.');
        if(index<0){
            return name;
        }
        return name.substring(index+1);
    }

    public String getArea(){
        int index = name.lastIndexOf('.');
        if(index<0){
            return "";
        }
        return name.substring(0,index);
    }

    public DockerClient getClient(){
        return client;
    }

    public Map<String,String> getLabels(){
        return Collections.unmodifiableMap(labels);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DockerNode dockerNode = (DockerNode) o;

        return name.equals(dockerNode.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "DockerNode{" +
                "name='" + name + '\'' +
                ", labels=" + labels +
                '}';
    }
}
