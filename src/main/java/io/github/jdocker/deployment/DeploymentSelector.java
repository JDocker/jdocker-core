package io.github.jdocker.deployment;

import java.util.Collection;
import java.util.Map;

/**
 * Created by atsticks on 13.01.16.
 */
public interface DeploymentSelector {

    Collection<Region> selectTargetRegions(Collection<Region> electedRegions, Map<String, String> attributes);
    
}
