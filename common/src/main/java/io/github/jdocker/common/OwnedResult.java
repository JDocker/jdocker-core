package io.github.jdocker.common;

import java.util.Objects;

/**
 * Created by atsticks on 02.02.16.
 */
public class OwnedResult {

    private String owner;
    private String type;
    private Object result;
    private long timestamp = System.currentTimeMillis();

    public OwnedResult(String owner, String type, Object result){
        this.owner = Objects.requireNonNull(owner);
        this.type = Objects.requireNonNull(type);
        this.result = Objects.requireNonNull(result);
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String getResultType() {
        return result.getClass().getName();
    }

    public Object getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OwnedResult{" +
                "owner='" + owner + '\'' +
                ", type='" + type + '\'' +
                ", result=" + result +
                ", timestamp=" + timestamp +
                '}';
    }
}
