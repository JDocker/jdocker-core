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
package io.github.jdocker.internal;

import com.google.common.base.Predicate;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.AuthConfig;
import io.github.jdocker.JDockerHost;
import io.github.jdocker.UnpooledMachine;
import io.github.jdocker.MachineConfig;
import io.github.jdocker.MachineConfigBuilder;
import io.github.jdocker.spi.HostRegistrySpi;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registry managing the docker registries known to this master orchestration node.
 */
public class DefaultHostRegistrySpi implements HostRegistrySpi {

    private static final Logger LOG = Logger.getLogger(DefaultHostRegistrySpi.class.getName());
    private static final Map<String, JDockerHost> DOCKERS = new ConcurrentHashMap<>();
    private static final Map<String, UnpooledMachine> UNPOOLED = new ConcurrentHashMap<>();

    public DefaultHostRegistrySpi(){
        // read master config from Tamaya
        Configuration config = ConfigurationProvider.getConfiguration();
        Map<String,String> props = config.getProperties();
        String masterDockers = props.get("jdocker.docker.masters");
        if(masterDockers!=null){
            for(String masterKey:masterDockers.split(",")){
                String[] labels = readLabelForDockerContainer(masterKey, config);
                DockerClient client = initDockerClient(masterKey, config);
                MachineConfig mc = MachineConfig.builder(masterKey).build();
                DOCKERS.put(masterKey, new DefaultDockerHost(mc));
                LOG.info("Registered docker instance: " + masterKey);
            }
        }
        String unpooledHosts = props.get("jdocker.docker.unpooled");
        if(unpooledHosts!=null){
            for(String masterKey:unpooledHosts.split(",")){

            }
        }
    }

    private JDockerHost initMachine(String name, Configuration config) {
        MachineConfig mConfig = readMachineConfig(config);
        DefaultDockerHost m = new DefaultDockerHost(mConfig);
        return m;
    }

    private MachineConfig readMachineConfig(Configuration config) {
        // TODO define and implement config format.
        return null;
    }

    private DockerClient initDockerClient(String dockerName, Configuration config) {
        DefaultDockerClient.Builder builder = DefaultDockerClient.builder();
        String val = config.get("jdocker.docker."+dockerName+".apiVersion");
        if(val!=null){
            builder.apiVersion(val);
        }
        val = config.get("jdocker.docker."+dockerName+".connectionPoolSize");
        if(val!=null){
            builder.connectionPoolSize(Integer.parseInt(val));
        }
        AuthConfig.Builder authConfBuilder = AuthConfig.builder();
        boolean useAuth = false;
        val = config.get("jdocker.docker."+dockerName+".auth.email");
        if(val!=null){authConfBuilder.email(val);useAuth=true;}
        val = config.get("jdocker.docker."+dockerName+".auth.userName");
        if(val!=null){authConfBuilder.username(val);useAuth=true;}
        val = config.get("jdocker.docker."+dockerName+".auth.serverAddress");
        if(val!=null){authConfBuilder.serverAddress(val);useAuth=true;}
        val = config.get("jdocker.docker."+dockerName+".auth.password");
        if(val!=null){authConfBuilder.password(val);useAuth=true;}
        if(useAuth){
            builder.authConfig(authConfBuilder.build());
        }
        val = config.get("jdocker.docker."+dockerName+".connectTimeoutMillis");
        if(val!=null){
            builder.connectTimeoutMillis(Long.parseLong(val));
        }
        val = config.get("jdocker.docker."+dockerName+".readTimeoutMillis");
        if(val!=null){
            builder.readTimeoutMillis(Long.parseLong(val));
        }
        val = config.get("jdocker.docker."+dockerName+".uri");
        if(val!=null){
            builder.uri(val);
        }
        DockerCertificates.Builder certBuilder = DockerCertificates.builder();
        boolean useCerts = false;
        val = config.get("jdocker.docker."+dockerName+".caCertPath");
        if(val!=null){certBuilder.caCertPath(new File(val).toPath());useCerts=true;}
        val = config.get("jdocker.docker."+dockerName+".clientCertPath");
        if(val!=null){certBuilder.clientCertPath(new File(val).toPath());useCerts=true;}
        val = config.get("jdocker.docker."+dockerName+".clientKeyPath");
        if(val!=null){certBuilder.clientKeyPath(new File(val).toPath());useCerts=true;}
        val = config.get("jdocker.docker."+dockerName+".dockerCertPath");
        if(val!=null){certBuilder.dockerCertPath(new File(val).toPath());useCerts=true;}
        if(useCerts) {
            try {
                com.google.common.base.Optional<DockerCertificates> optCerts = certBuilder.build();
                if(optCerts.isPresent()) {
                    builder.dockerCertificates(optCerts.get());
                }
            } catch (DockerCertificateException e) {
                LOG.log(Level.SEVERE, "Failed to initialize docker certificates for DockerClient.", e);
            }
        }
        return builder.build();
    }

    private String[] readLabelForDockerContainer(String dockerName, Configuration config) {
        List<String> labels = new ArrayList<>();
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
                labels.add(key+'='+value);
            }
        }
        return labels.toArray(new String[labels.size()]);
    }

    @Override
    public JDockerHost addDocker(String name, DockerClient client, String... labels){
        MachineConfigBuilder builder = new MachineConfigBuilder(name);
        DefaultDockerHost dockerHost = new DefaultDockerHost(builder.build());
        DefaultHostRegistrySpi.DOCKERS.put(name, dockerHost);
        return dockerHost;
    }

    @Override
    public JDockerHost getDocker(String name){
        return DefaultHostRegistrySpi.DOCKERS.remove(name);
    }

    @Override
    public Collection<JDockerHost> getDockerMachines(Predicate<JDockerHost> predicate) {
        List<JDockerHost> found = new ArrayList<>();
        for(JDockerHost host:this.getDockerMachines()){
            if(predicate.apply(host)){
                found.add(host);
            }
        }
        return found;
    }

    @Override
    public Collection<JDockerHost> getDockerMachines(){
        return DefaultHostRegistrySpi.DOCKERS.values();
    }

    @Override
    public Set<String> getDockerHostNames(){
        return DefaultHostRegistrySpi.DOCKERS.keySet();
    }

    @Override
    public JDockerHost removeDockerHost(String name){
        return DefaultHostRegistrySpi.DOCKERS.remove(name);
    }

    @Override
    public void removeDockerHosts(Predicate<JDockerHost> predicate) {
        List<JDockerHost> found = new ArrayList<>();
        for(JDockerHost host:this.getDockerMachines()){
            if(predicate.apply(host)){
                DefaultHostRegistrySpi.DOCKERS.remove(host.getName());
            }
        }
    }

    @Override
    public void addHost(UnpooledMachine unpooledMachine) {

    }

    @Override
    public Collection<UnpooledMachine> getHosts() {
        return null;
    }

    @Override
    public UnpooledMachine getHost(String address) {
        return null;
    }

    @Override
    public int getPooledHostCount() {
        return 0;
    }

    @Override
    public int getUnpooledHostCount() {
        return 0;
    }

}
