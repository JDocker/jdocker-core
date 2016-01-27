package io.github.jdocker;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by atsticks on 24.01.16.
 */
public class DockerHostBuilder{

    String name;
    URI uri;
    Map<String,String> properties = new HashMap<>();
    int cpus;
    int memory;
    long disksize;
    MachineStatus status = MachineStatus.Unknown;


    public DockerHostBuilder(URI uri) {
        this(uri.toString(), uri);
    }

    public DockerHostBuilder(String name, URI uri) {
        this.uri = Objects.requireNonNull(uri);
        this.name = name==null?uri.toString():name;
    }

    public DockerHostBuilder setCpus(int cpus) {
        this.cpus = cpus;
        return this;
    }

    public DockerHostBuilder setMemory(int memory) {
        this.memory = memory;
        return this;
    }

    public DockerHostBuilder setDisksize(long disksize) {
        this.disksize = disksize;
        return this;
    }

    public DockerHostBuilder setStatus(MachineStatus status) {
        this.status = Objects.requireNonNull(status);
        return this;
    }

    public DockerHostBuilder setProperties(Map<String,String> properties){
        this.properties.putAll(Objects.requireNonNull(properties));
        return this;
    }

    public DockerHostBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DockerHostBuilder setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public DockerHostBuilder addProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    public DockerHostBuilder removeProperty(String key) {
        this.properties.remove(key);
        return this;
    }

    public DockerHost build(){
        return new DockerHost(this);
    }

    @Override
    public String toString() {
        return "DockerHostBuilder{" +
                "cpus=" + cpus +
                ", memory=" + memory +
                ", disksize=" + disksize +
                ", status=" + status +
                '}';
    }
}
