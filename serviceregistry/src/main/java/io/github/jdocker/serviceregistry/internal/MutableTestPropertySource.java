package io.github.jdocker.serviceregistry.internal;

import org.apache.tamaya.spi.PropertySource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by atsticks on 07.02.16.
 */
public final class MutableTestPropertySource implements PropertySource{

    private static final Map<String, String> SHARED_MAP = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "MutableTestProperties";
    }

    @Override
    public Map<String, String> getProperties() {
        return SHARED_MAP;
    }

    public static Map<String, String> getSharedConfig(){
        return SHARED_MAP;
    }
}
