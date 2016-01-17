package io.github.jdocker.networking;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by atsticks on 17.01.16.
 */
public class Peer{

    private String name;
    private String nickName;
    private long id;
    private long shortId;
    private int version;
    private Set<String> connections = new HashSet<>();


    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public long getId() {
        return id;
    }

    public long getShortId() {
        return shortId;
    }

    public int getVersion() {
        return version;
    }

    public Set<String> getConnections() {
        return connections;
    }
}
